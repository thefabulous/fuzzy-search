package co.thefabulous.search.engine;

import android.support.v4.util.ArrayMap;

import java.util.Map;
import java.util.Objects;

import co.thefabulous.search.fuse.Options;

import static co.thefabulous.search.util.Precondition.checkPointer;

/**
 * Decorator for scoring any object.
 */
public class ScoredObject<T extends Indexable> implements Comparable<ScoredObject> {
    private final T object;
    private final Map<Integer, Options.SearchResult> fieldsSearchResults; //keys are indices of fields
    private Double score;
    private boolean hasMatches;

    public ScoredObject(T object) {
        this.object = object;
        fieldsSearchResults = new ArrayMap<>();
    }

    public boolean hasMatches() {
        return hasMatches;
    }

    public ScoredObject setHasMatches(boolean hasMatches) {
        this.hasMatches = hasMatches;
        return this;
    }

    public Map<Integer, Options.SearchResult> getFieldsSearchResults() {
        return fieldsSearchResults;
    }

    public ScoredObject addSearchResult(int fieldIndex, Options.SearchResult searchResult) {
        fieldsSearchResults.put(fieldIndex, searchResult);
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
                && Objects.equals(fieldsSearchResults, other.fieldsSearchResults);
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
        return Objects.hash(object, hasMatches, score, fieldsSearchResults);
    }
}
