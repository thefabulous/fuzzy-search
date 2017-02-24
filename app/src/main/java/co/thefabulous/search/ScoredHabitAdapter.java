package co.thefabulous.search;


import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.thefabulous.search.engine.ScoredObject;
import co.thefabulous.search.engine.SearchResult;
import co.thefabulous.search.util.ImmutablePair;

public class ScoredHabitAdapter extends ArrayAdapter<ScoredObject<Habit>> {
    final String prefix = "<font color=\'#E91E63\'><b>";
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

        setText(item, titleTextView, 0);
        setText(item, subtitleTextView, 1);

        // Return the completed view to render on screen
        return convertView;
    }

    private void setText(ScoredObject<Habit> item, TextView textView, int index) {
        SearchResult searchResultTitle = item.getFieldsSearchResults().get(index);
        if (searchResultTitle != null
                && searchResultTitle.matchedIndices() != null
                && !searchResultTitle.matchedIndices().isEmpty()) {
            String text = item.getObject().getFields().get(index);
            textView.setText(Html.fromHtml(getHighlightedString(searchResultTitle, text)));
        } else {
            textView.setText(item.getObject().getFields().get(index));
        }
    }

    private String getHighlightedString(SearchResult searchResult, String text) {
        List<ImmutablePair<Integer, Integer>> matchedIndices = searchResult.matchedIndices();
//        Collections.sort(matchedIndices, new Comparator<Pair<Integer, Integer>>() {
//            @Override
//            public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
//                return Integer.compare(o2.second - o2.first, o1.second - o1.first);
//            }
//        });

        int offset = 0;
        for (ImmutablePair<Integer, Integer> machIndexes : matchedIndices) {
            text = text.substring(0, offset + machIndexes.first)
                    + prefix
                    + text.substring(offset + machIndexes.first, offset + machIndexes.second + 1)
                    + suffix
                    + text.substring(offset + machIndexes.second + 1, text.length());

            offset += prefix.length() + suffix.length();
        }
        Log.d("Highlight", text);
        return text;
    }
}
