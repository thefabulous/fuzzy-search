package co.thefabulous.search.search;


import java.util.Collection;

import javax.annotation.Nullable;

import co.thefabulous.search.search.data.ScoredObject;

/**
 * Adapter for any index data structure.
 */
public interface IndexAdapter<T> {
    /**
     * Returns a {@link Collection} of all values associated with a token.
     */
    Collection<ScoredObject<T>> get(String token);

    /**
     * Associates a single value with a token.
     */
    boolean put(String token, @Nullable T value);

    /**
     * Removes a single value associated with any tokens.
     */
    boolean remove(T value);
}
