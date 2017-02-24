package co.thefabulous.search.fuse;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import co.thefabulous.search.bitap.BitapFactory;
import co.thefabulous.search.fuse.data.Indexable;
import co.thefabulous.search.fuse.data.ScoredObject;

import static co.thefabulous.search.fuse.Options.SearchFunction;
import static co.thefabulous.search.fuse.Options.SearchResult;
import static co.thefabulous.search.util.Precondition.checkArgument;

/**
 * Created by Bartosz Lipinski
 * 23.02.2017
 */
public class FuseEngine<T extends Indexable, P extends ScoredObject<T>> implements Engine<T, P> {

    public static final Options DEFAULT_OPTIONS = Options.builder()
            .caseSensitive(false)
            .include(new ArrayList<String>())
            .shouldSort(true)
            .searchFunction(new BitapFactory())
            .sortFunction(new Options.SortFunction() {
                @Override
                int sort(IndexableSearchResult a, IndexableSearchResult b) {
                    return Double.compare(a.score, b.score);
                }
            })
            .keys(new ArrayList<String>())
            .verbose(false)
            .tokenize(false)
            .matchAllTokens(false)
            .tokenSeparator("\\s+")
            .minimumCharLength(1)
            .findAllMatches(false)
            .build();

    @NonNull
    private final Options options;
    @VisibleForTesting List<Options.SearchFunction> tokenSearchers;
    @VisibleForTesting SearchFunction fullSearcher;
    @VisibleForTesting List<IndexableSearchResult<T>> results;
    @VisibleForTesting Map<Integer, IndexableSearchResult<T>> resultMap;
    private Collection<T> dataSet;

    public FuseEngine(@NonNull Options options) {
        //noinspection ConstantConditions
        checkArgument(options != null, "options cannot be null");
        this.options = options;
        mergeOptionsWithDefault(options);
    }

    private void mergeOptionsWithDefault(Options options) {

        //// TODO: 23.02.2017
    }

    private void log(String stringToFormat, Object... args) {
        if (options.verbose) {
            Log.v("FuseEngine", String.format(stringToFormat, args));
        }
    }

    @Override
    public boolean addAll(Collection<T> dataSet) {
        this.dataSet = dataSet;
        return true;
    }

    @Override
    public List<P> search(String pattern) {
        log("\nSearch term: %s\n", pattern);

        results = new ArrayList<>();
        resultMap = new ArrayMap<>();

        prepareSearchers(pattern);
        startSearch();
        computeScore();
        sort();
        return format();
    }

    @VisibleForTesting
    void prepareSearchers(String pattern) {
        tokenSearchers = new ArrayList<>();
        if (options.tokenize) {
            final String[] tokens = pattern.split(options.tokenSeparator);
            for (String token : tokens) {
                tokenSearchers.add(options.searchFunction.getSearchFunction(token, options));
            }
        }
        fullSearcher = options.searchFunction.getSearchFunction(pattern, options);
    }

    private void startSearch() {
        int i = 0;
        for (T t : dataSet) {
            final List<String> fields = t.getFields();
            for (int j = 0; j < fields.size(); j++) {
                analyze(fields.get(j), t, i);
            }
            i++;
        }
    }

    @VisibleForTesting
    void analyze(String text, T entity, int index) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        boolean exists = false;
        List<Double> scores = new ArrayList<>();
        int numTextMatches = 0;
        Double averageScore = null;

        final String[] words = text.split(options.tokenSeparator);
        if (this.options.tokenize) {
            for (SearchFunction tokenSearcher : tokenSearchers) {
                log("Pattern: %s", tokenSearcher.pattern());

                List<Map<String, Double>> termScores = new ArrayList<>();
                boolean hasMatchInText = false;

                for (String word : words) {
                    SearchResult tokenSearchResult = tokenSearcher.search(word, false);
                    Map<String, Double> obj = new ArrayMap<>();
                    if (tokenSearchResult.isMatch()) {
                        obj.put(word, tokenSearchResult.score());
                        exists = true;
                        hasMatchInText = true;
                        scores.add(tokenSearchResult.score());
                    } else {
                        obj.put(word, 1.0);
                        if (!options.matchAllTokens) {
                            scores.add(1.0);
                        }
                    }
                    termScores.add(obj);
                }

                if (hasMatchInText) {
                    numTextMatches++;
                }
//               log("Token scores: %s", termScores); //// TODO: 23.02.2017 log this array

                averageScore = scores.get(0);
                int scoresLen = scores.size();
                for (int i = 1; i < scoresLen; i++) {
                    averageScore += scores.get(i);
                }
                averageScore = averageScore / (double) scoresLen;

                log("Token scores average: %f", averageScore);
            }
        }

        final SearchResult mainSearchResult = fullSearcher.search(text, true);
        log("Full text score: %f", mainSearchResult.score());

        final double finalScore = (averageScore != null) ?
                (mainSearchResult.score() + averageScore) / 2.0 :
                mainSearchResult.score();

        log("Score average %f", finalScore);

        boolean checkTextMatches = !(this.options.tokenize && this.options.matchAllTokens) ||
                numTextMatches >= this.tokenSearchers.size();

        log("Check Matches %b", checkTextMatches);

        // If a match is found, add the item to <rawResults>, including its score
        if ((exists || mainSearchResult.isMatch()) && checkTextMatches) {
            // Check if the item already exists in our results
            IndexableSearchResult indexableSearchResult = resultMap.get(index);

            final SearchResult searchResult = new SearchResult() {
                @Override
                public boolean isMatch() {
                    return true;
                }

                @Override
                public double score() {
                    return finalScore;
                }

                @Override
                public List<Pair<Integer, Integer>> matchedIndices() {
                    return mainSearchResult.matchedIndices();
                }
            };
            if (indexableSearchResult != null) {
                // Use the lowest score
                // existingResult.score, bitapResult.score
                indexableSearchResult.fieldsResults.add(searchResult);
            } else {
                // Add it to the raw result list
                indexableSearchResult = new IndexableSearchResult(entity, searchResult);
                resultMap.put(index, indexableSearchResult);
                results.add(indexableSearchResult);
            }
        }
    }

    void computeScore() {
        log("\n\nComputing score:\n");

        for (int i = 0, size = results.size(); i < size; i++) {
            double totalScore = 0;
            List<SearchResult> output = results.get(i).fieldsResults;
            int scoreLen = output.size();

            for (int j = 0; j < scoreLen; j++) {
                totalScore += output.get(j).score();
            }

            results.get(i).score = totalScore / scoreLen;
//            log(results.get(i));
        }
    }

    private void sort() {
        if (options.shouldSort) {
            log("\n\nSorting");
            Collections.sort(results, options.sortFunction);
        }
    }

    private List<P> format() {
        // TODO: 23.02.2017
        return null;
    }

    public static class IndexableSearchResult<T> {
        public double score;
        T entity;
        List<SearchResult> fieldsResults;

        public IndexableSearchResult(T entity, SearchResult result) {
            this.entity = entity;
            this.fieldsResults = new ArrayList<>();
            this.fieldsResults.add(result);
        }
    }
}
