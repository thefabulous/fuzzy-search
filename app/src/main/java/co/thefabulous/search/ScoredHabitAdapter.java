package co.thefabulous.search;


import android.content.Context;
import android.support.v4.util.Pair;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import co.thefabulous.search.fuse.Options;
import co.thefabulous.search.fuse.data.ScoredObject;

public class ScoredHabitAdapter extends ArrayAdapter<ScoredObject<Habit>> {
    final String prefix = "<font color='#E91E63'><b>";
    final String suffix = "</b></font>";

    public ScoredHabitAdapter(Context context, List<ScoredObject<Habit>> habits) {
        super(context, 0, habits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ScoredObject<Habit> item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        // Lookup view for data population
        TextView titleTextView = (TextView) convertView.findViewById(R.id.title_textview);
        TextView subtitleTextView = (TextView) convertView.findViewById(R.id.subtitle_textview);
        // Populate the data into the template view using the data object

        Options.SearchResult searchResultTitle = item.getFieldsSearchResults().get(0);
        if (searchResultTitle != null
                && searchResultTitle.matchedIndices() != null
                && !searchResultTitle.matchedIndices().isEmpty()) {
            String title = item.getObject().getTitle();
            titleTextView.setText(Html.fromHtml(getHighlightedString(searchResultTitle, title)));
        } else {
            titleTextView.setText(item.getObject().getTitle());
        }

        Options.SearchResult searchResultSubtitle = item.getFieldsSearchResults().get(1);
        if (searchResultSubtitle != null
                && searchResultSubtitle.matchedIndices() != null
                && !searchResultSubtitle.matchedIndices().isEmpty()) {
            String text = item.getObject().getSubtitle();
            subtitleTextView.setText(Html.fromHtml(getHighlightedString(searchResultSubtitle, text)));
        } else {
            subtitleTextView.setText(item.getObject().getSubtitle());
        }


        // Return the completed view to render on screen
        return convertView;
    }

    private String getHighlightedString(Options.SearchResult searchResult, String text) {
        int offset = 0;
        Collections.sort(searchResult.matchedIndices(), new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
                return Integer.compare(o2.second - o2.first, o1.second - o1.first);
            }
        });
//        for (Pair<Integer, Integer> machIndexes : searchResult.matchedIndices()) {
//            if (!machIndexes.first.equals(machIndexes.second)) {
        Pair<Integer, Integer> machIndexes = searchResult.matchedIndices().get(0);
                text = text.substring(0, offset + machIndexes.first)
                        + prefix
                        + text.substring(offset + machIndexes.first, offset + machIndexes.second + 1)
                        + suffix
                        + text.substring(offset + machIndexes.second + 1, text.length());

                offset += prefix.length() + suffix.length();
//            }
//        }
        return text;
    }
}
