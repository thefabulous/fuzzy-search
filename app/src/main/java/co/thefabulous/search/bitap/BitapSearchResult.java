package co.thefabulous.search.bitap;


import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

import co.thefabulous.search.engine.SearchResult;

public class BitapSearchResult implements SearchResult {
    boolean isMatch;
    double score;
    List<Pair<Integer, Integer>> matchedIndices;

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
    public List<Pair<Integer, Integer>> matchedIndices() {
        return matchedIndices;
    }

    public static final class Builder {
        private boolean isMatch;
        private double score;
        private List<Pair<Integer, Integer>> matchedIndices;

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

        public Builder matchedIndice(Pair<Integer, Integer> val) {
            if (matchedIndices == null) {
                matchedIndices = new ArrayList<>();
            }
            matchedIndices.add(val);
            return this;
        }

        public Builder matchedIndices(List<Pair<Integer, Integer>> val) {
            matchedIndices = val;
            return this;
        }

        public BitapSearchResult build() {
            return new BitapSearchResult(this);
        }
    }
}
