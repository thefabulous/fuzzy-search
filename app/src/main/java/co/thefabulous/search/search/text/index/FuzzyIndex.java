package co.thefabulous.search.search.text.index;

import java.util.Set;

import co.thefabulous.search.search.data.ScoredObject;
import co.thefabulous.search.search.text.match.Automaton;

/**
 * {@link Index} with approximate key matching.
 */
public interface FuzzyIndex<V> extends Index<V> {
    /**
     * Returns a {@link Set} of all values associated with a key fragment.
     *
     * @throws NullPointerException if {@code fragment} is null;
     */
    Set<ScoredObject<V>> getAny(String fragment);

    /**
     * Returns a {@link Set} of all values associated with a key matcher.
     *
     * @throws NullPointerException if {@code matcher} is null;
     */
    Set<ScoredObject<V>> getAny(Automaton matcher);
}
