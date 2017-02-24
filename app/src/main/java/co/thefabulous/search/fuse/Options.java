package co.thefabulous.search.fuse;

import java.util.Comparator;

import javax.annotation.Nullable;

import co.thefabulous.search.engine.ScoredObject;
import co.thefabulous.search.engine.SearchFunction;

/**
 * Created by Bartosz Lipinski
 * 23.02.2017
 */
public class Options {

    // available only from within the package
    SearchFunctionFactory searchFunction;
    SortFunction sortFunction;
    Logger logger;

    // available from outside
    public Boolean caseSensitive;
    public Integer minimumCharLength;
    public Boolean shouldSort;
    public Boolean tokenize;
    public Boolean matchAllTokens;
    public Boolean findAllMatches;
    public Integer location;
    public Float threshold;
    public Integer distance;
    public Integer maxPatternLength;

    public Options(SearchFunctionFactory searchFunction, SortFunction sortFunction, Logger logger, Boolean caseSensitive, Integer minimumCharLength, Boolean shouldSort, Boolean tokenize, Boolean matchAllTokens, Boolean findAllMatches, Integer location, Float threshold, Integer distance, Integer maxPatternLength) {
        this.searchFunction = searchFunction;
        this.sortFunction = sortFunction;
        this.logger = logger;
        this.caseSensitive = caseSensitive;
        this.minimumCharLength = minimumCharLength;
        this.shouldSort = shouldSort;
        this.tokenize = tokenize;
        this.matchAllTokens = matchAllTokens;
        this.findAllMatches = findAllMatches;
        this.location = location;
        this.threshold = threshold;
        this.distance = distance;
        this.maxPatternLength = maxPatternLength;
    }

    public static Builder builder() {
        return new Builder();
    }

    static Builder builder(Options options) {
        return new Builder(options);
    }

    public Options mergeWith(Options other) {
        searchFunction = searchFunction != null ? searchFunction : other.searchFunction;
        sortFunction = sortFunction != null ? sortFunction : other.sortFunction;
        logger = logger != null ? logger : other.logger;
        caseSensitive = caseSensitive != null ? caseSensitive : other.caseSensitive;
        minimumCharLength = minimumCharLength != null ? minimumCharLength : other.minimumCharLength;
        shouldSort = shouldSort != null ? shouldSort : other.shouldSort;
        tokenize = tokenize != null ? tokenize : other.tokenize;
        matchAllTokens = matchAllTokens != null ? matchAllTokens : other.matchAllTokens;
        findAllMatches = findAllMatches != null ? findAllMatches : other.findAllMatches;
        location = location != null ? location : other.location;
        threshold = threshold != null ? threshold : other.threshold;
        distance = distance != null ? distance : other.distance;
        maxPatternLength = maxPatternLength != null ? maxPatternLength : other.maxPatternLength;
        return this;
    }

    @Override
    public String toString() {
        return "Options{"
                + "searchFunction=" + searchFunction + ", "
                + "sortFunction=" + sortFunction + ", "
                + "verbose=" + logger + ", "
                + "caseSensitive=" + caseSensitive + ", "
                + "minimumCharLength=" + minimumCharLength + ", "
                + "shouldSort=" + shouldSort + ", "
                + "tokenize=" + tokenize + ", "
                + "matchAllTokens=" + matchAllTokens + ", "
                + "findAllMatches=" + findAllMatches + ", "
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
            return ((this.searchFunction == null) ? (that.searchFunction == null) : this.searchFunction.equals(that.searchFunction))
                    && ((this.sortFunction == null) ? (that.sortFunction == null) : this.sortFunction.equals(that.sortFunction))
                    && ((this.logger == null) ? (that.logger == null) : this.logger.equals(that.logger))
                    && ((this.caseSensitive == null) ? (that.caseSensitive == null) : this.caseSensitive.equals(that.caseSensitive))
                    && ((this.minimumCharLength == null) ? (that.minimumCharLength == null) : this.minimumCharLength.equals(that.minimumCharLength))
                    && ((this.shouldSort == null) ? (that.shouldSort == null) : this.shouldSort.equals(that.shouldSort))
                    && ((this.tokenize == null) ? (that.tokenize == null) : this.tokenize.equals(that.tokenize))
                    && ((this.matchAllTokens == null) ? (that.matchAllTokens == null) : this.matchAllTokens.equals(that.matchAllTokens))
                    && ((this.findAllMatches == null) ? (that.findAllMatches == null) : this.findAllMatches.equals(that.findAllMatches))
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
        h ^= (searchFunction == null) ? 0 : this.searchFunction.hashCode();
        h *= 1000003;
        h ^= (sortFunction == null) ? 0 : this.sortFunction.hashCode();
        h *= 1000003;
        h ^= (logger == null) ? 0 : this.logger.hashCode();
        h *= 1000003;
        h ^= (caseSensitive == null) ? 0 : this.caseSensitive.hashCode();
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
        private Boolean caseSensitive;
        private Integer minimumCharLength;
        private Boolean shouldSort;
        private Boolean tokenize;
        private Boolean matchAllTokens;
        private Boolean findAllMatches;
        private Integer location;
        private Float threshold;
        private Integer distance;
        private Integer maxPatternLength;
        private Logger logger;

        public Builder() {
        }

        public Builder(Options source) {
            this.searchFunction = source.searchFunction;
            this.sortFunction = source.sortFunction;
            this.logger = source.logger;
            this.caseSensitive = source.caseSensitive;
            this.minimumCharLength = source.minimumCharLength;
            this.shouldSort = source.shouldSort;
            this.tokenize = source.tokenize;
            this.matchAllTokens = source.matchAllTokens;
            this.findAllMatches = source.findAllMatches;
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

        public Builder caseSensitive(@Nullable Boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
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

        public Builder logger(@Nullable Logger logger) {
            this.logger = logger;
            return this;
        }

        public Options build() {
            return new Options(
                    this.searchFunction,
                    this.sortFunction,
                    this.logger,
                    this.caseSensitive,
                    this.minimumCharLength,
                    this.shouldSort,
                    this.tokenize,
                    this.matchAllTokens,
                    this.findAllMatches,
                    this.location,
                    this.threshold,
                    this.distance,
                    this.maxPatternLength);
        }
    }

    public interface SearchFunctionFactory {
        SearchFunction getSearchFunction(String pattern, Options options);
    }

    public interface Logger {
        void log(String stringToFormat, Object... args);
    }

    static abstract class SortFunction implements Comparator<ScoredObject<?>> {
        abstract int sort(ScoredObject<?> a, ScoredObject<?> b);

        @Override
        public int compare(ScoredObject<?> o1, ScoredObject<?> o2) {
            return sort(o1, o2);
        }
    }
}