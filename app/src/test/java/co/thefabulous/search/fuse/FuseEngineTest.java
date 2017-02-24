package co.thefabulous.search.fuse;

import android.support.v4.util.ArrayMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import co.thefabulous.search.Habit;
import co.thefabulous.search.bitap.BitapSearcher;
import co.thefabulous.search.engine.Indexable;
import co.thefabulous.search.engine.ScoredObject;
import co.thefabulous.search.engine.SearchResult;

import static co.thefabulous.search.fuse.FuseEngine.DEFAULT_OPTIONS;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by Bartosz Lipinski
 * 23.02.2017
 */
@RunWith(JUnit4.class)
public class FuseEngineTest {

    @Test
    public void prepareSearchers_whenTokenize_setsProperSearchers() throws Exception {
        FuseEngine<Habit> fuseEngine = new FuseEngine<>(Options.builder(DEFAULT_OPTIONS)
                .tokenize(true).caseSensitive(true).build());

        assertThat(fuseEngine.tokenSearchers).isNull();
        assertThat(fuseEngine.fullSearcher).isNull();

        fuseEngine.prepareSearchers("Squeeze han");

        assertThat(fuseEngine.fullSearcher.pattern()).isEqualTo("Squeeze han");
        assertThat(fuseEngine.tokenSearchers.size()).isEqualTo(2);
        assertThat(fuseEngine.tokenSearchers.get(0).pattern()).isEqualTo("Squeeze");
        assertThat(fuseEngine.tokenSearchers.get(1).pattern()).isEqualTo("han");
    }

    @Test
    public void whenNotTokenizing_analyze_storesResults_inMap() {
        FuseEngine<Habit> fuseEngine = new FuseEngine<>(Options.builder(DEFAULT_OPTIONS)
                .tokenize(true).build());

        Map<Integer, ScoredObject<Habit>> resultMap = new ArrayMap<>();
        fuseEngine.prepareSearchers("br");

        final Indexable indexable = new Indexable() {
            @Override
            public List<String> getFields() {
                return Arrays.asList("The Book of Lies", "Brad");
            }
        };
        int indexableIndex = 0;
        fuseEngine.analyze(resultMap, indexable.getFields().get(0), null, indexableIndex, 0);
        fuseEngine.analyze(resultMap, indexable.getFields().get(1), null, indexableIndex, 1);

        assertThat(resultMap.size()).isEqualTo(1);
        assertThat(resultMap.get(0).getFieldsSearchResults().size()).isEqualTo(2);
    }

    @Test
    public void checkResultWithBitapSearcher() {
        String pattern = "Clench";
        final String text = "Clench your Fists or Squeeze your Hands";

        BitapSearcher bitapSearcher = new BitapSearcher(pattern, FuseEngine.DEFAULT_OPTIONS);
        SearchResult searchResult = bitapSearcher.search(text);

        FuseEngine<Habit> fuseEngine = new FuseEngine<>(Options.builder(DEFAULT_OPTIONS)
                .tokenize(false).build());

        Map<Integer, ScoredObject<Habit>> resultMap = new ArrayMap<>();
        fuseEngine.prepareSearchers(pattern);

        final Indexable indexable = new Indexable() {
            @Override
            public List<String> getFields() {
                return Arrays.asList(text);
            }
        };
        int indexableIndex = 0;

        fuseEngine.analyze(resultMap, indexable.getFields().get(0), null, indexableIndex, 0);
        fuseEngine.computeScore(resultMap);

        List<ScoredObject<Habit>> results = new ArrayList<>(resultMap.values());

        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getFieldsSearchResults().size()).isEqualTo(1);

        assertThat(results.get(0).getScore()).isEqualTo(searchResult.score());
    }

    @Test(expected = IllegalStateException.class)
    public void search_throwsException_whenDataSetNotSet() {
        FuseEngine<Habit> fuseEngine = new FuseEngine<>(Options.builder(DEFAULT_OPTIONS)
                .tokenize(true).build());
        fuseEngine.search("Clench");
    }

    @Test
    public void search_analyzes_everyField_of_everyDataSetEntry() {
        FuseEngine<Indexable> fuseEngine =
                spy(new FuseEngine<Indexable>(Options.builder(DEFAULT_OPTIONS)
                        .tokenize(true).build()));

        // build dataset:
        List<Indexable> dataset = new ArrayList<>();
        final Indexable indexable1 = new Indexable() {
            @Override
            public List<String> getFields() {
                final ArrayList<String> fields = new ArrayList<>(2);
                fields.add("Indexable 1 -> Test field 1");
                fields.add("Indexable 1 -> Test field 2");
                fields.add("Indexable 1 -> Test field 3");
                return fields;
            }
        };
        final Indexable indexable2 = new Indexable() {
            @Override
            public List<String> getFields() {
                final ArrayList<String> fields = new ArrayList<>(2);
                fields.add("Indexable 2 -> Test field 1");
                return fields;
            }
        };
        dataset.add(indexable1);
        dataset.add(indexable2);

        // set dataset:
        fuseEngine.useDataSet(dataset);

        // reset spied instance
        reset(fuseEngine);

        // search
        final String searched = "something";
        fuseEngine.search(searched);

        // verify all major calls
        verify(fuseEngine, times(1)).search(searched);
        verify(fuseEngine, times(1)).prepareSearchers(searched);
        //noinspection unchecked
        verify(fuseEngine, times(1)).startSearch(any(Map.class));
        //noinspection unchecked
        verify(fuseEngine, times(1)).computeScore(any(Map.class));
        //noinspection unchecked
        verify(fuseEngine, times(1)).sort(any(List.class));

        // verify calls specific to startSearch
        //noinspection unchecked
        verify(fuseEngine, times(1))
                .analyze(any(Map.class), eq("Indexable 1 -> Test field 1"), eq(indexable1), eq(0), eq(0));
        //noinspection unchecked
        verify(fuseEngine, times(1))
                .analyze(any(Map.class), eq("Indexable 1 -> Test field 2"), eq(indexable1), eq(0), eq(1));
        //noinspection unchecked
        verify(fuseEngine, times(1))
                .analyze(any(Map.class), eq("Indexable 1 -> Test field 3"), eq(indexable1), eq(0), eq(2));
        //noinspection unchecked
        verify(fuseEngine, times(1))
                .analyze(any(Map.class), eq("Indexable 2 -> Test field 1"), eq(indexable2), eq(1), eq(0));
        verifyNoMoreInteractions(fuseEngine);
    }
}