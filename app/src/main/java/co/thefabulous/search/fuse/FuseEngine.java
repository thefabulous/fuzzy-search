package co.thefabulous.search.fuse;

import java.util.Collection;
import java.util.List;

import co.thefabulous.search.search.data.Indexable;

/**
 * Created by Bartosz Lipinski
 * 23.02.2017
 */
public class FuseEngine<T extends Indexable> implements Engine<T> {
    @Override
    public boolean addAll(Collection<T> dataSet) {
        return false;
    }

    @Override
    public List<T> search(String query) {
        return null;
    }
}
