package co.thefabulous.search.engine;

import android.support.v4.util.Pair;

import java.util.List;

/**
 * Created by Bartosz Lipinski
 * 24.02.2017
 */
public interface SearchResult {
    boolean isMatch();

    double score();

    List<Pair<Integer, Integer>> matchedIndices();
}
