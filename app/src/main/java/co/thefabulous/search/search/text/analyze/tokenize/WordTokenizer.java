package co.thefabulous.search.search.text.analyze.tokenize;


import java.text.BreakIterator;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import co.thefabulous.search.search.text.analyze.Analyzer;

import static co.thefabulous.search.search.common.Precondition.checkPointer;

/**
 * Break text into words.
 */
public class WordTokenizer extends Analyzer {
    private final BreakIterator boundary;

    /**
     * Constructs a new {@link WordTokenizer}.
     */
    public WordTokenizer() {
        this.boundary = BreakIterator.getWordInstance();
    }

    @Override
    public Collection<String> apply(Collection<String> input) {
        checkPointer(input != null);
        List<String> result = new LinkedList<>();
        for (String text : input) {
            checkPointer(text != null);
            boundary.setText(text.toString());
            for (
                    int start = boundary.first(), end = boundary.next();
                    end != BreakIterator.DONE;
                    start = end, end = boundary.next()
                    ) {
                String word = text.substring(start, end);
                if (Character.isLetterOrDigit(word.charAt(0))) {
                    result.add(word);
                }
            }
        }
        return result;
    }
}
