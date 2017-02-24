package co.thefabulous.search.fuse;

import java.util.Collection;
import java.util.List;

import co.thefabulous.search.fuse.data.Indexable;
import co.thefabulous.search.fuse.data.ScoredObject;

public interface Engine<T extends Indexable, P extends ScoredObject<T>> {
    boolean addAll(Collection<T> dataSet);
    List<P> search(String query);
}
