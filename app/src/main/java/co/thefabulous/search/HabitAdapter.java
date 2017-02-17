package co.thefabulous.search;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.thefabulous.search.simplesearch.fuzzywuzzy.FuzzyMatch;
import co.thefabulous.search.simplesearch.fuzzywuzzy.ScoredObject;

import static com.google.common.base.Preconditions.checkNotNull;

public class HabitAdapter extends ArrayAdapter<ScoredObject<Habit>> {
    public HabitAdapter(Context context, List<ScoredObject<Habit>> habits) {
        super(context, 0, habits);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final ScoredObject<Habit> scoredObject = getItem(position);
        checkNotNull(scoredObject);
        final Habit habit = scoredObject.getObject();
        checkNotNull(habit);
        final List<String> matches = scoredObject.getMatchingWords();
        checkNotNull(matches);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        // Lookup view for data population
        TextView titleTextView = (TextView) convertView.findViewById(R.id.title_textview);
        TextView subtitleTextView = (TextView) convertView.findViewById(R.id.subtitle_textview);

        // Populate the data into the template view using the data object
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            titleTextView.setText(Html.fromHtml(boldMatchingStrings(habit.getTitle(), matches), Html.FROM_HTML_MODE_COMPACT));
            subtitleTextView.setText(Html.fromHtml(boldMatchingStrings(habit.getSubtitle(), matches), Html.FROM_HTML_MODE_COMPACT));
        } else {
            titleTextView.setText(Html.fromHtml(boldMatchingStrings(habit.getTitle(), matches)));
            subtitleTextView.setText(Html.fromHtml(boldMatchingStrings(habit.getSubtitle(), matches)));
        }
        // Return the completed view to render on screen
        return convertView;
    }

    private static String boldMatchingStrings(String original, List<String> matches) {
        for (String s : matches) {
            final String processedString = FuzzyMatch.processString(original).toLowerCase();

            if (processedString.contains(s)) {
                final int start = processedString.indexOf(s);
                final int end = start + s.length();
                final String substringToBold = original.substring(start, end);
                original = original.replaceAll(substringToBold, boldify(substringToBold));
            }
        }
        return original;
    }

    @NonNull
    private static String boldify(String s) {
        return String.format("<b>%s</b>", s);
    }
}
