package co.thefabulous.search.bitap;

import java.util.ArrayList;
import java.util.List;

import co.thefabulous.search.engine.SearchResult;
import co.thefabulous.search.util.ImmutablePair;

public class BitapSearchResult implements SearchResult {
    boolean isMatch;
    double score;
    List<ImmutablePair<Integer, Integer>> matchedIndices;

    private BitapSearchResult(Builder builder) {
        isMatch = builder.isMatch;
        score = builder.score;
        matchedIndices = builder.matchedIndices;
    }

    @Override
    public boolean isMatch() {
        return isMatch;
    }

    @Override
    public double score() {
        return score;
    }

    @Override
    public List<ImmutablePair<Integer, Integer>> matchedIndices() {
        return matchedIndices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitapSearchResult that = (BitapSearchResult) o;

        if (isMatch != that.isMatch) return false;
        if (Double.compare(that.score, score) != 0) return false;
        return matchedIndices != null ? indicesEqualWith(that.matchedIndices) : that.matchedIndices == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (isMatch ? 1 : 0);
        temp = Double.doubleToLongBits(score);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (matchedIndices != null ? matchedIndices.hashCode() : 0);
        return result;
    }

    private boolean indicesEqualWith(List<ImmutablePair<Integer, Integer>> otherIndicesList) {
        if (matchedIndices.size() != otherIndicesList.size()) {
            return false;
        }
        for (int i = 0; i < matchedIndices.size(); i++) {
            final ImmutablePair<Integer, Integer> thisIndices = matchedIndices.get(i);
            final ImmutablePair<Integer, Integer> otherIndices = otherIndicesList.get(i);
            if (otherIndices == null) { //couldn't find indices for this i
                return false;
            }
            if (!thisIndices.equals(otherIndices)){
                return false;
            }
        }
        return true;
    }

    public static final class Builder {
        private boolean isMatch;
        private double score;
        private List<ImmutablePair<Integer, Integer>> matchedIndices;

        public Builder() {
        }

        public Builder isMatch(boolean val) {
            isMatch = val;
            return this;
        }

        public Builder score(double val) {
            score = val;
            return this;
        }

        public Builder matchedIndice(ImmutablePair<Integer, Integer> val) {
            if (matchedIndices == null) {
                matchedIndices = new ArrayList<>();
            }
            matchedIndices.add(val);
            return this;
        }

        public Builder matchedIndices(List<ImmutablePair<Integer, Integer>> val) {
            matchedIndices = val;
            return this;
        }

        public BitapSearchResult build() {
            return new BitapSearchResult(this);
        }
    }
}
