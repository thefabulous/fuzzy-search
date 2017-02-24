package co.thefabulous.search.fuse;

import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Bartosz Lipinski
 * 23.02.2017
 */
public class Options {

    // available only from within the package
    String id;
    SearchFunctionFactory searchFunction;
    SortFunction sortFunction;
    Boolean verbose;
    String tokenSeparator;

    // available from outside
    public Boolean caseSensitive;
    public List<String> include;
    public Integer minimumCharLength;
    public Boolean shouldSort;
    public Boolean tokenize;
    public Boolean matchAllTokens;
    public Boolean findAllMatches;
    public List<String> keys;
    public Integer location;
    public Float threshold;
    public Integer distance;
    public Integer maxPatternLength;

    public Options( SearchFunctionFactory searchFunction, SortFunction sortFunction, Boolean verbose, String tokenSeparator, Boolean caseSensitive, List<String> include, Integer minimumCharLength, Boolean shouldSort, Boolean tokenize, Boolean matchAllTokens, Boolean findAllMatches, List<String> keys, Integer location, Float threshold, Integer distance, Integer maxPatternLength) {
        this.searchFunction = searchFunction;
        this.sortFunction = sortFunction;
        this.verbose = verbose;
        this.tokenSeparator = tokenSeparator;
        this.caseSensitive = caseSensitive;
        this.include = include;
        this.minimumCharLength = minimumCharLength;
        this.shouldSort = shouldSort;
        this.tokenize = tokenize;
        this.matchAllTokens = matchAllTokens;
        this.findAllMatches = findAllMatches;
        this.keys = keys;
        this.location = location;
        this.threshold = threshold;
        this.distance = distance;
        this.maxPatternLength = maxPatternLength;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Options options) {
        return new Builder(options);
    }

    @Override
    public String toString() {
        return "Options{"
                + "id=" + id + ", "
                + "searchFunction=" + searchFunction + ", "
                + "sortFunction=" + sortFunction + ", "
                + "verbose=" + verbose + ", "
                + "tokenSeparator=" + tokenSeparator + ", "
                + "caseSensitive=" + caseSensitive + ", "
                + "include=" + include + ", "
                + "minimumCharLength=" + minimumCharLength + ", "
                + "shouldSort=" + shouldSort + ", "
                + "tokenize=" + tokenize + ", "
                + "matchAllTokens=" + matchAllTokens + ", "
                + "findAllMatches=" + findAllMatches + ", "
                + "keys=" + keys + ", "
                + "location=" + location + ", "
                + "threshold=" + threshold + ", "
                + "distance=" + distance + ", "
                + "maxPatternLength=" + maxPatternLength
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Options) {
            Options that = (Options) o;
            return ((this.id == null) ? (that.id == null) : this.id.equals(that.id))
                    && ((this.searchFunction == null) ? (that.searchFunction == null) : this.searchFunction.equals(that.searchFunction))
                    && ((this.sortFunction == null) ? (that.sortFunction == null) : this.sortFunction.equals(that.sortFunction))
                    && ((this.verbose == null) ? (that.verbose == null) : this.verbose.equals(that.verbose))
                    && ((this.tokenSeparator == null) ? (that.tokenSeparator == null) : this.tokenSeparator.equals(that.tokenSeparator))
                    && ((this.caseSensitive == null) ? (that.caseSensitive == null) : this.caseSensitive.equals(that.caseSensitive))
                    && ((this.include == null) ? (that.include == null) : this.include.equals(that.include))
                    && ((this.minimumCharLength == null) ? (that.minimumCharLength == null) : this.minimumCharLength.equals(that.minimumCharLength))
                    && ((this.shouldSort == null) ? (that.shouldSort == null) : this.shouldSort.equals(that.shouldSort))
                    && ((this.tokenize == null) ? (that.tokenize == null) : this.tokenize.equals(that.tokenize))
                    && ((this.matchAllTokens == null) ? (that.matchAllTokens == null) : this.matchAllTokens.equals(that.matchAllTokens))
                    && ((this.findAllMatches == null) ? (that.findAllMatches == null) : this.findAllMatches.equals(that.findAllMatches))
                    && ((this.keys == null) ? (that.keys == null) : this.keys.equals(that.keys))
                    && ((this.location == null) ? (that.location == null) : this.location.equals(that.location))
                    && ((this.threshold == null) ? (that.threshold == null) : this.threshold.equals(that.threshold))
                    && ((this.distance == null) ? (that.distance == null) : this.distance.equals(that.distance))
                    && ((this.maxPatternLength == null) ? (that.maxPatternLength == null) : this.maxPatternLength.equals(that.maxPatternLength));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : this.id.hashCode();
        h *= 1000003;
        h ^= (searchFunction == null) ? 0 : this.searchFunction.hashCode();
        h *= 1000003;
        h ^= (sortFunction == null) ? 0 : this.sortFunction.hashCode();
        h *= 1000003;
        h ^= (verbose == null) ? 0 : this.verbose.hashCode();
        h *= 1000003;
        h ^= (tokenSeparator == null) ? 0 : this.tokenSeparator.hashCode();
        h *= 1000003;
        h ^= (caseSensitive == null) ? 0 : this.caseSensitive.hashCode();
        h *= 1000003;
        h ^= (include == null) ? 0 : this.include.hashCode();
        h *= 1000003;
        h ^= (minimumCharLength == null) ? 0 : this.minimumCharLength.hashCode();
        h *= 1000003;
        h ^= (shouldSort == null) ? 0 : this.shouldSort.hashCode();
        h *= 1000003;
        h ^= (tokenize == null) ? 0 : this.tokenize.hashCode();
        h *= 1000003;
        h ^= (matchAllTokens == null) ? 0 : this.matchAllTokens.hashCode();
        h *= 1000003;
        h ^= (findAllMatches == null) ? 0 : this.findAllMatches.hashCode();
        h *= 1000003;
        h ^= (keys == null) ? 0 : this.keys.hashCode();
        h *= 1000003;
        h ^= (location == null) ? 0 : this.location.hashCode();
        h *= 1000003;
        h ^= (threshold == null) ? 0 : this.threshold.hashCode();
        h *= 1000003;
        h ^= (distance == null) ? 0 : this.distance.hashCode();
        h *= 1000003;
        h ^= (maxPatternLength == null) ? 0 : this.maxPatternLength.hashCode();
        return h;
    }

    public static final class Builder {
        private SearchFunctionFactory searchFunction;
        private SortFunction sortFunction;
        private Boolean verbose;
        private String tokenSeparator;
        private Boolean caseSensitive;
        private List<String> include;
        private Integer minimumCharLength;
        private Boolean shouldSort;
        private Boolean tokenize;
        private Boolean matchAllTokens;
        private Boolean findAllMatches;
        private List<String> keys;
        private Integer location;
        private Float threshold;
        private Integer distance;
        private Integer maxPatternLength;

        public Builder() {
        }

        public Builder(Options source) {
            this.searchFunction = source.searchFunction;
            this.sortFunction = source.sortFunction;
            this.verbose = source.verbose;
            this.tokenSeparator = source.tokenSeparator;
            this.caseSensitive = source.caseSensitive;
            this.include = new ArrayList<>(source.include);
            this.minimumCharLength = source.minimumCharLength;
            this.shouldSort = source.shouldSort;
            this.tokenize = source.tokenize;
            this.matchAllTokens = source.matchAllTokens;
            this.findAllMatches = source.findAllMatches;
            this.keys = new ArrayList<>(source.keys);
            this.location = source.location;
            this.threshold = source.threshold;
            this.distance = source.distance;
            this.maxPatternLength = source.maxPatternLength;
        }

        public Builder searchFunction(@Nullable SearchFunctionFactory searchFunction) {
            this.searchFunction = searchFunction;
            return this;
        }

        public Builder sortFunction(@Nullable SortFunction sortFunction) {
            this.sortFunction = sortFunction;
            return this;
        }

        public Builder verbose(@Nullable Boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        public Builder tokenSeparator(@Nullable String tokenSeparator) {
            this.tokenSeparator = tokenSeparator;
            return this;
        }

        public Builder caseSensitive(@Nullable Boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
            return this;
        }

        public Builder include(@Nullable List<String> include) {
            this.include = include;
            return this;
        }

        public Builder minimumCharLength(@Nullable Integer minimumCharLength) {
            this.minimumCharLength = minimumCharLength;
            return this;
        }

        public Builder shouldSort(@Nullable Boolean shouldSort) {
            this.shouldSort = shouldSort;
            return this;
        }

        public Builder tokenize(@Nullable Boolean tokenize) {
            this.tokenize = tokenize;
            return this;
        }

        public Builder matchAllTokens(@Nullable Boolean matchAllTokens) {
            this.matchAllTokens = matchAllTokens;
            return this;
        }

        public Builder findAllMatches(@Nullable Boolean findAllMatches) {
            this.findAllMatches = findAllMatches;
            return this;
        }

        public Builder keys(@Nullable List<String> keys) {
            this.keys = keys;
            return this;
        }

        public Builder location(@Nullable Integer location) {
            this.location = location;
            return this;
        }

        public Builder threshold(@Nullable Float threshold) {
            this.threshold = threshold;
            return this;
        }

        public Builder distance(@Nullable Integer distance) {
            this.distance = distance;
            return this;
        }

        public Builder maxPatternLength(@Nullable Integer maxPatternLength) {
            this.maxPatternLength = maxPatternLength;
            return this;
        }

        public Options build() {
            return new Options(
                    this.searchFunction,
                    this.sortFunction,
                    this.verbose,
                    this.tokenSeparator,
                    this.caseSensitive,
                    this.include,
                    this.minimumCharLength,
                    this.shouldSort,
                    this.tokenize,
                    this.matchAllTokens,
                    this.findAllMatches,
                    this.keys,
                    this.location,
                    this.threshold,
                    this.distance,
                    this.maxPatternLength);
        }
    }

    public interface SearchResult {
        boolean isMatch();

        double score();

        List<Pair<Integer, Integer>> matchedIndices();
    }

    public interface SearchFunction {
        SearchResult search(String text);

        String pattern();
    }

    public interface SearchFunctionFactory {
        SearchFunction getSearchFunction(String pattern, Options options);
    }

    static abstract class SortFunction implements Comparator<FuseEngine.IndexableSearchResult> {
        abstract int sort(FuseEngine.IndexableSearchResult a, FuseEngine.IndexableSearchResult b);

        @Override
        public int compare(FuseEngine.IndexableSearchResult a, FuseEngine.IndexableSearchResult b) {
            return sort(a, b);
        }
    }
}