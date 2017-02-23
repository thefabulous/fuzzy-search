package co.thefabulous.search.fuse;

import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * Created by Bartosz Lipinski
 * 23.02.2017
 */
@AutoValue
public abstract class Options {

    public interface SearchResult {
        boolean isMatch();

        double score();

        List<Pair<Integer, Integer>> matchedIndices();
    }

    public interface SearchFunction{
        SearchResult search(String text);
    }

    public interface SearchFunctionFactory{
        SearchFunction getSearchFunction(String pattern, Options options);
    }

    interface SortFunction {
        int search(SearchResult a, SearchResult b);
    }

    interface GetFunction {
        void get(Object obj, Object path, Object list); // TODO: 23.02.2017 determine types
    }

    // available only from within the package
    @Nullable
    abstract String id();

    @Nullable
    abstract SearchFunctionFactory searchFunction();

    @Nullable
    abstract SortFunction sortFunction();

    @Nullable
    abstract GetFunction getFunction();

    @Nullable
    abstract Boolean verbose();

    @Nullable
    abstract String tokenSeparator();

    // available from outside

    @Nullable
    public abstract Boolean caseSensitive();

    @Nullable
    public abstract List<String> include();

    @Nullable
    public abstract int minimumCharLength();

    @Nullable
    public abstract Boolean shouldSort();

    @Nullable
    public abstract Boolean tokenize();

    @Nullable
    public abstract Boolean matchAllTokens();

    @Nullable
    public abstract Boolean findAllMatches();

    @Nullable
    public abstract List<String> keys();

    @Nullable
    public abstract Integer location();

    @Nullable
    public abstract Float threshold();

    @Nullable
    public abstract Integer distance();

    @Nullable
    public abstract Integer maxPatternLength();

    public static Builder builder() {
        return new AutoValue_Options.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        abstract Builder id(String id);

        abstract Builder searchFunction(SearchFunctionFactory searchFunction);

        abstract Builder sortFunction(SortFunction sortFunction);

        abstract Builder getFunction(GetFunction newFunction);

        abstract Builder verbose(Boolean verbose);

        abstract Builder tokenSeparator(String tokenSeparator);

        public abstract Builder caseSensitive(Boolean caseSensitive);

        public abstract Builder include(List<String> include);

        public abstract Builder minimumCharLength(int minimumCharLength);

        public abstract Builder shouldSort(Boolean shouldSort);

        public abstract Builder tokenize(Boolean tokenize);

        public abstract Builder matchAllTokens(Boolean matchAllTokens);

        public abstract Builder findAllMatches(Boolean findAllMatches);

        public abstract Builder keys(List<String> keys);

        public abstract Builder location(Integer location);

        public abstract Builder threshold(Float threshold);

        public abstract Builder distance(Integer distance);

        public abstract Builder maxPatternLength(Integer maxPatternLength);

        public abstract Options build();
    }
}