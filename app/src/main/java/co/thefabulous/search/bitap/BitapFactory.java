package co.thefabulous.search.bitap;


import co.thefabulous.search.fuse.Options;

public class BitapFactory implements Options.SearchFunctionFactory {
    @Override
    public Options.SearchFunction getSearchFunction(String pattern, Options options) {
        return new BitapSearcher(pattern, options);
    }
}
