package co.thefabulous.search.search.text.analyze.filter;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import co.thefabulous.search.search.text.analyze.Analyzer;

import static co.thefabulous.search.search.common.Precondition.checkPointer;

/**
 * Exclude text with length outside boundaries.
 */
public class LengthFilter extends Analyzer {
    private final int min, max;

    /**
     * Constructs a new {@link LengthFilter}.
     */
    public LengthFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Collection<String> apply(Collection<String> input) {
        checkPointer(input != null);
        List<String> result = new LinkedList<>();
        for (String text : input) {
            checkPointer(text != null);
            if (text.length() >= min && text.length() <= max) {
                result.add(text);
            }
        }
        return result;
    }
}
