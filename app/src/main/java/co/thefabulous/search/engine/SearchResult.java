package co.thefabulous.search.engine;

import java.util.List;

import co.thefabulous.search.util.ImmutablePair;

/**
 * Created by Bartosz Lipinski
 * 24.02.2017
 */
public interface SearchResult {
    boolean isMatch();

    double score();

    List<ImmutablePair<Integer, Integer>> matchedIndices();
}
