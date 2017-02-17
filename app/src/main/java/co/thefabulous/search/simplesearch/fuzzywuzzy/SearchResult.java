package co.thefabulous.search.simplesearch.fuzzywuzzy;

import java.util.List;

public class SearchResult {
    private final List<String> matchingWords;
    private final int ratio;

    public SearchResult(List<String> matchingWords, int ratio) {
        this.matchingWords = matchingWords;
        this.ratio = ratio;
    }

    public List<String> getMatchingWords() {
        return matchingWords;
    }

    public int getScore() {
        return ratio;
    }
}
