package co.thefabulous.search.simplesearch.fuzzywuzzy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import co.thefabulous.search.Habit;
import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

public class HabitsSearchEngine {

    private static final double SCORE_TRESHOLD = 0;
    private final List<Habit> habits = new ArrayList<>();

    public void setHabits(Collection<Habit> habits) {
        this.habits.clear();
        this.habits.addAll(habits);
    }

    public Observable<List<ScoredObject<Habit>>> search(final String query) {
        checkNotNull(query);

        return Observable.from(habits)
                .flatMap(habit -> {
                    final SearchResult searchResult =
                            FuzzyMatch.search(getTitleWithSubtitle(habit), query);
                    return Observable.just(
                            new ScoredObject<>(habit,
                                    searchResult.getScore(),
                                    searchResult.getMatchingWords()))
                            ;
                })
                .filter(habitScoredObject -> habitScoredObject.getScore() > SCORE_TRESHOLD)
                .toSortedList((habitWithScore1, habitWithScore2) ->
                        (habitWithScore1.compareTo(habitWithScore2)));
    }

    private static String getTitleWithSubtitle(Habit habit) {
        return String.format("%s %s", habit.getTitle(), habit.getSubtitle());
    }

}
