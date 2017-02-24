package co.thefabulous.search.fuse.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.thefabulous.search.fuse.Options;

import static co.thefabulous.search.util.Precondition.checkPointer;

/**
 * Decorator for scoring any object.
 */
public class ScoredObject<T extends Indexable> implements Comparable<ScoredObject> {
    private final T object;
    private final List<Options.SearchResult> searchResults;
    private Double score;
    private boolean hasMatches;

    public ScoredObject(T object) {
        this.object = object;
        searchResults = new ArrayList<>();
    }

    public boolean hasMatches() {
        return hasMatches;
    }

    public ScoredObject setHasMatches(boolean hasMatches) {
        this.hasMatches = hasMatches;
        return this;
    }

    public List<Options.SearchResult> getSearchResults() {
        return searchResults;
    }

    public ScoredObject addSerachResult(Options.SearchResult searchResult, int index) {
        searchResults.add(index, searchResult);
        return this;
    }

    /**
     * Returns the decorated object.
     */
    public T getObject() {
        return object;
    }

    /**
     * Returns the score.
     */
    public double getScore() {
        return score;
    }

    public ScoredObject setScore(Double score) {
        this.score = score;
        return this;
    }

    @Override
    public int compareTo(ScoredObject other) {
        checkPointer(other != null);
        return Double.compare(other.score, score);
    }

    private boolean equals(ScoredObject<?> other) {
        assert other != null;
        return Objects.equals(object, other.object)
                && Objects.equals(hasMatches, other.hasMatches)
                && Objects.equals(score, other.score)
                && Objects.equals(searchResults, other.searchResults);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ScoredObject)) {
            return false;
        }
        return equals((ScoredObject<?>) other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, hasMatches, score, searchResults);
    }
}
