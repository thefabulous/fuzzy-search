package co.thefabulous.search.search.text.analyze.transform;


import java.text.Normalizer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import co.thefabulous.search.search.text.analyze.Analyzer;

import static co.thefabulous.search.search.common.Precondition.checkPointer;

/**
 * Strip text diacritics.
 */
public class DiacriticsTransformer extends Analyzer {
    @Override
    public Collection<String> apply(Collection<String> input) {
        checkPointer(input != null);
        List<String> result = new LinkedList<>();
        for (String text : input) {
            checkPointer(text != null);
            StringBuilder builder = new StringBuilder();
            String canonical = Normalizer.normalize(text, Normalizer.Form.NFD);
            for (int i = 0; i < canonical.length(); ++i) {
                if (Character.getType(canonical.charAt(i)) != Character.NON_SPACING_MARK) {
                    builder.append(canonical.charAt(i));
                }
            }
            result.add(builder.toString());
        }
        return result;
    }
}
