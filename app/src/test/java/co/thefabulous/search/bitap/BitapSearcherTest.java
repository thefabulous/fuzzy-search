package co.thefabulous.search.bitap;

import org.junit.Test;

import co.thefabulous.search.engine.SearchResult;
import co.thefabulous.search.fuse.FuseEngine;
import co.thefabulous.search.fuse.Options;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BitapSearcherTest {
    @Test
    public void search() throws Exception {
        BitapSearcher bitapSearcher = new BitapSearcher("Clench your Fists or Squeeze your Hands And Toto", FuseEngine.DEFAULT_OPTIONS);
        SearchResult searchResult = bitapSearcher.search("Clench your Fists or Squeeze your Hands");

        assertThat(searchResult.isMatch(), is(true));
    }

}