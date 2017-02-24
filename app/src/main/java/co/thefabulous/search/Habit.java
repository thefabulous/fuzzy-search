package co.thefabulous.search;


import java.util.Arrays;
import java.util.List;

import co.thefabulous.search.fuse.data.Indexable;

public class Habit implements Indexable {
    String title;
    String subtitle;

    public Habit(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public List<String> getFields() {
        return Arrays.asList(title, subtitle);
    }
}
