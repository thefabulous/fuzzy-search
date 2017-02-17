package co.thefabulous.search.search.text.analyze.transform;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import co.thefabulous.search.search.text.analyze.Analyzer;

import static co.thefabulous.search.search.common.Precondition.checkPointer;

public class NonAlphaCharsTransformer extends Analyzer {

    @Override
    public Collection<String> apply(Collection<String> input) {
        checkPointer(input != null);
        List<String> result = new LinkedList<>();
        for (String text : input) {
            checkPointer(text != null);
            result.add(removeNonAlphaChars(text));
        }
        return result;
    }

    private static String removeNonAlphaChars(String token) {

        StringBuffer s = new StringBuffer(token.length());

        CharacterIterator it = new StringCharacterIterator(token);
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
            switch (ch) {
                // '-,)(!`\":/][?;~><
                case '\'':
                case '/':
                case '\\':
                case '-':
                case ',':
                case ')':
                case '(':
                case '!':
                case '`':
                case '\"':
                case ':':
                case ']':
                case '[':
                case '?':
                case ';':
                case '~':
                case '<':
                case '>':
                    s.append(" ");
                    break;
                default:
                    s.append(ch);
                    break;
            }
        }

        token = s.toString();
        return token;
    }
}
