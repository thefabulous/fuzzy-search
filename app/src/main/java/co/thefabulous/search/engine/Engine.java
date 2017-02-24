package co.thefabulous.search.engine;

import java.util.Collection;
import java.util.List;

import co.thefabulous.search.engine.Indexable;
import co.thefabulous.search.engine.ScoredObject;

public interface Engine<T extends Indexable> {
    void useDataSet(Collection<T> dataSet);
    List<ScoredObject<T>> search(String query);
}
