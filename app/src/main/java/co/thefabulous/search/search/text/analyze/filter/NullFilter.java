package co.thefabulous.search.search.text.analyze.filter;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import co.thefabulous.search.search.text.analyze.Analyzer;

import static co.thefabulous.search.search.common.Precondition.checkPointer;

/**
 * Exclude null text.
 */
public class NullFilter extends Analyzer {
    @Override
    public Collection<String> apply(Collection<String> input) {
        checkPointer(input != null);
        List<String> result = new LinkedList<>();
        for (String text : input) {
            if (text != null) {
                result.add(text);
            }
        }
        return result;
    }
}
