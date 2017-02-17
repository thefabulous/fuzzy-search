package co.thefabulous.search;


import java.util.Collection;

import co.thefabulous.search.search.text.analyze.Analyzer;
import co.thefabulous.search.search.text.analyze.tokenize.WordTokenizer;
import co.thefabulous.search.search.text.analyze.transform.LowerCaseTransformer;
import co.thefabulous.search.search.text.analyze.transform.NonAlphaCharsTransformer;

public class SampleAnalyzer extends Analyzer {
    private Analyzer tokenizer = new WordTokenizer();
    private Analyzer transformer = new LowerCaseTransformer();
    private Analyzer nonAlphaCharsTransformer = new NonAlphaCharsTransformer();

    @Override
    public Collection<String> apply(Collection<String> input) {
        return tokenizer.apply(transformer.apply(nonAlphaCharsTransformer.apply(input)));
    }
}
