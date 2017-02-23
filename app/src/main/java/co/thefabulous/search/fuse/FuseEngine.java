package co.thefabulous.search.fuse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import co.thefabulous.search.search.data.Indexable;

/**
 * Created by Bartosz Lipinski
 * 23.02.2017
 */
public class FuseEngine<T extends Indexable> implements Engine<T> {

    private final Options DEFAULT_OPTIONS = Options.builder()
            .id(null)
            .caseSensitive(false)
            .include(new ArrayList<String>())
            .shouldSort(true)
            .searchFunction(null) //// TODO: 23.02.2017 set BitapSearcher
            .sortFunction(new Options.SortFunction() {
                @Override
                public int sort(Options.SearchResult a, Options.SearchResult b) {
                    return a.score() - b.score();
                }
            })
            .getFunction(new Options.GetFunction() {
                @Override
                public void get(Object obj, Object path, Object list) {
                    //// TODO: 23.02.2017
                }
            })
            .keys(new ArrayList<String>())
            .verbose(false)
            .tokenize(false)
            .matchAllTokens(false)
            .tokenSeparator("/ +/g") //// TODO: 23.02.2017 check if correct
            .minimumCharLength(1)
            .findAllMatches(false)
            .build();
    @Override
    public boolean addAll(Collection<T> dataSet) {
        return false;
    }

    @Override
    public List<T> search(String query) {
        return null;
    }
}
