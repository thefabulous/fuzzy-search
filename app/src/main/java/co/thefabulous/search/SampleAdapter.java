package co.thefabulous.search;


import java.util.Collection;

import co.thefabulous.search.search.IndexAdapter;
import co.thefabulous.search.search.data.ScoredObject;
import co.thefabulous.search.search.text.index.FuzzyIndex;
import co.thefabulous.search.search.text.index.PatriciaTrie;
import co.thefabulous.search.search.text.match.EditDistanceAutomaton;

public class SampleAdapter implements IndexAdapter<Habit> {
    private FuzzyIndex<Habit> index = new PatriciaTrie<>();

    @Override
    public Collection<ScoredObject<Habit>> get(String token) {
        double threshold = Math.log(token.length() - 1);
        return index.getAny(new EditDistanceAutomaton(token, threshold));
    }

    @Override
    public boolean put(String token, Habit value) {
        return index.put(token, value);
    }

    @Override
    public boolean remove(Habit value) {
        return index.remove(value);
    }
}
