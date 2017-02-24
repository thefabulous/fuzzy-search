package co.thefabulous.search.fuse;

import android.support.v4.util.ArrayMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.thefabulous.search.Habit;
import co.thefabulous.search.fuse.data.Indexable;
import co.thefabulous.search.fuse.data.ScoredObject;

import static co.thefabulous.search.fuse.FuseEngine.DEFAULT_OPTIONS;
import static com.google.common.truth.Truth.assertThat;

/**
 * Created by Bartosz Lipinski
 * 23.02.2017
 */
@RunWith(RobolectricTestRunner.class)
public class FuseEngineTest {

    @Test
    public void prepareSearchers_whenTokenize_setsProperSearchers() throws Exception {
        FuseEngine<Habit, ScoredObject<Habit>> fuseEngine = new FuseEngine<>(Options.builder(DEFAULT_OPTIONS)
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
    public void whenNotTokenizing_analyze_storesResults_inMap_and_inList() {
        FuseEngine<Habit, ScoredObject<Habit>> fuseEngine = new FuseEngine<>(Options.builder(DEFAULT_OPTIONS)
                .tokenize(true).build());

        fuseEngine.results = new ArrayList<>();
        fuseEngine.resultMap = new ArrayMap<>();
        fuseEngine.prepareSearchers("br");

        final Indexable indexable = new Indexable() {
            @Override
            public List<String> getFields() {
                return Arrays.asList("The Book of Lies", "Brad");
            }
        };
        int indexableIndex = 0;
        fuseEngine.analyze(indexable.getFields().get(0), null, indexableIndex);
        fuseEngine.analyze(indexable.getFields().get(1), null, indexableIndex);

        assertThat(fuseEngine.results.size()).isEqualTo(1);
        assertThat(fuseEngine.results.get(0).fieldsResults.size()).isEqualTo(2);

        assertThat(fuseEngine.resultMap.size()).isEqualTo(1);
        assertThat(fuseEngine.resultMap.get(0).fieldsResults.size()).isEqualTo(2);
    }

}