package co.thefabulous.search.fuse;

import java.util.Collection;
import java.util.List;

import co.thefabulous.search.search.data.Indexable;

public interface Engine<T extends Indexable> {
    boolean addAll(Collection<T> dataSet);
    List<T> search(String query);
}
