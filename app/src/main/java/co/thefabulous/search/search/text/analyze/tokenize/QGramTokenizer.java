package co.thefabulous.search.search.text.analyze.tokenize;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import co.thefabulous.search.search.text.analyze.Analyzer;

import static co.thefabulous.search.search.common.Precondition.checkPointer;

/**
 * Break text into q-grams (also known as n-grams).
 */
public class QGramTokenizer extends Analyzer {
    private final int size;

    /**
     * Constructs a new {@link QGramTokenizer}.
     */
    public QGramTokenizer(int size) {
        this.size = size;
    }

    @Override
    public Collection<String> apply(Collection<String> input) {
        checkPointer(input != null);
        List<String> result = new LinkedList<>();
        for (String text : input) {
            checkPointer(text != null);
            for (int i = 0; i + size <= text.length(); ++i) {
                result.add(text.substring(i, i + size));
            }
        }
        return result;
    }
}
