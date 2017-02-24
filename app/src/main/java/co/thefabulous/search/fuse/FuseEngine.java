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
import co.thefabulous.search.bitap.WordTokenizer;
import co.thefabulous.search.fuse.data.Indexable;
import co.thefabulous.search.fuse.data.ScoredObject;

import static co.thefabulous.search.fuse.Options.SearchFunction;
import static co.thefabulous.search.fuse.Options.SearchResult;
import static co.thefabulous.search.util.Precondition.checkArgument;

/**
 * Created by Bartosz Lipinski
 * 23.02.2017
 */
public class FuseEngine<T extends Indexable> implements Engine<T> {

    public static final Options DEFAULT_OPTIONS = Options.builder()
            .caseSensitive(false)
            .shouldSort(true)
            .searchFunction(new BitapFactory())
            .sortFunction(new Options.SortFunction() {
                @Override
                int sort(ScoredObject<?> a, ScoredObject<?> b) {
                    return b.compareTo(a);
                }
            })
            .verbose(false)
            .tokenize(false)
            .matchAllTokens(false)
            .minimumCharLength(1)
            .findAllMatches(false)
            .build();

    @NonNull
    private final Options options;
    @VisibleForTesting List<Options.SearchFunction> tokenSearchers;
    @VisibleForTesting SearchFunction fullSearcher;
    private Collection<T> dataSet;
    @VisibleForTesting List<ScoredObject<T>> results;
    @VisibleForTesting Map<Integer, ScoredObject<T>> resultMap;

    public FuseEngine(@NonNull Options options) {
        //noinspection ConstantConditions
        checkArgument(options != null, "options cannot be null");
        this.options = options;
        mergeOptionsWithDefault(options);
    }

    private void mergeOptionsWithDefault(Options options) {
        options.searchFunction = options.searchFunction != null ? options.searchFunction : DEFAULT_OPTIONS.searchFunction;
        options.sortFunction = options.sortFunction != null ? options.sortFunction : DEFAULT_OPTIONS.sortFunction;
        options.verbose = options.verbose != null ? options.verbose : DEFAULT_OPTIONS.verbose;
        options.caseSensitive = options.caseSensitive != null ? options.caseSensitive : DEFAULT_OPTIONS.caseSensitive;
        options.minimumCharLength = options.minimumCharLength != null ? options.minimumCharLength : DEFAULT_OPTIONS.minimumCharLength;
        options.shouldSort = options.shouldSort != null ? options.shouldSort : DEFAULT_OPTIONS.shouldSort;
        options.tokenize = options.tokenize != null ? options.tokenize : DEFAULT_OPTIONS.tokenize;
        options.matchAllTokens = options.matchAllTokens != null ? options.matchAllTokens : DEFAULT_OPTIONS.matchAllTokens;
        options.findAllMatches = options.findAllMatches != null ? options.findAllMatches : DEFAULT_OPTIONS.findAllMatches;
        options.location = options.location != null ? options.location : DEFAULT_OPTIONS.location;
        options.threshold = options.threshold != null ? options.threshold : DEFAULT_OPTIONS.threshold;
        options.distance = options.distance != null ? options.distance : DEFAULT_OPTIONS.distance;
        options.maxPatternLength = options.maxPatternLength != null ? options.maxPatternLength : DEFAULT_OPTIONS.maxPatternLength;
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
    public List<ScoredObject<T>> search(String pattern) {
        log("\nSearch term: %s\n", pattern);

        results = new ArrayList<>();
        resultMap = new ArrayMap<>();

        prepareSearchers(pattern);
        startSearch();
        computeScore();
        sort();
        return results;
    }

    @VisibleForTesting
    void prepareSearchers(String pattern) {
        tokenSearchers = new ArrayList<>();
        if (options.tokenize) {
            WordTokenizer tokenizer = new WordTokenizer();
            final Collection<String> tokens = tokenizer.apply(pattern);
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
                analyze(fields.get(j), t, i, j);
            }
            i++;
        }
    }

    @VisibleForTesting
    void analyze(String text, T indexable, int indexableIndex, int fieldIndex) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        boolean exists = false;
        List<Double> scores = new ArrayList<>();
        int numTextMatches = 0;
        Double averageScore = null;

        WordTokenizer tokenizer = new WordTokenizer();
        final Collection<String> words = tokenizer.apply(text);
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
            ScoredObject<T> scoredObject = resultMap.get(indexableIndex);

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
            if (scoredObject != null) {
                // Use the lowest score
                // existingResult.score, bitapResult.score
                scoredObject.addSearchResult(fieldIndex, searchResult);
            } else {
                // Add it to the raw result list
                scoredObject = new ScoredObject<>(indexable);
                scoredObject.addSearchResult(fieldIndex, searchResult);
                resultMap.put(indexableIndex, scoredObject);
                results.add(scoredObject);
            }
        }
    }

    void computeScore() {
        log("\n\nComputing score:\n");

        for (int i = 0, size = results.size(); i < size; i++) {
            double totalScore = 0;
            Map<Integer, SearchResult> output = results.get(i).getFieldsSearchResults();
            for (SearchResult value : output.values()) {
                totalScore += value.score();
            }
            results.get(i).setScore(totalScore / (double) output.size());
            log(results.get(i).toString());
        }
    }

    private void sort() {
        if (options.shouldSort) {
            log("\n\nSorting");
            Collections.sort(results, options.sortFunction);
        }
    }
}
