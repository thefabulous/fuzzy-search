package co.thefabulous.search.engine;

import android.support.v4.util.ArrayMap;

import java.util.Map;

import static co.thefabulous.search.util.Precondition.checkPointer;

/**
 * Decorator for scoring any object.
 */
public class ScoredObject<T extends Indexable> implements Comparable<ScoredObject> {
    private final T object;
    private final Map<Integer, SearchResult> fieldsSearchResults; //keys are indices of fields
    private Double score;

    public ScoredObject(T object) {
        this.object = object;
        fieldsSearchResults = new ArrayMap<>();
    }

    public Map<Integer, SearchResult> getFieldsSearchResults() {
        return fieldsSearchResults;
    }

    public ScoredObject addSearchResult(int fieldIndex, SearchResult searchResult) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoredObject<?> that = (ScoredObject<?>) o;

        if (!object.equals(that.object)) return false;
        if (!fieldsEqualsWith(that.getFieldsSearchResults())) return false;
        return score != null ? score.equals(that.score) : that.score == null;
    }

    private boolean fieldsEqualsWith(Map<Integer, SearchResult> otherFieldsSearchResults) {
        if (otherFieldsSearchResults == null || fieldsSearchResults.size() != otherFieldsSearchResults.size()) {
            return false;
        }
        for (Integer fieldIndex : fieldsSearchResults.keySet()) {
            final SearchResult thisSearchResult = fieldsSearchResults.get(fieldIndex);
            final SearchResult otherSearchResult = otherFieldsSearchResults.get(fieldIndex);
            if (otherSearchResult == null) { //couldn't find searchResults for this fieldIndex
                return false;
            }
            if (!thisSearchResult.equals(otherSearchResult)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = object.hashCode();
        result = 31 * result + fieldsSearchResults.hashCode();
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}
