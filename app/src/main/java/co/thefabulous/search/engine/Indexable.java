package co.thefabulous.search.engine;

import java.util.List;

/**
 * Unit of indexing and search.
 */
public interface Indexable {
    /**
     * Returns a {@link List} of indexable fields.
     */
    List<String> getFields();
}
