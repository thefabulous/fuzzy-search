package co.thefabulous.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import co.thefabulous.search.commons.TextWatcherAdapter;
import co.thefabulous.search.search.AutocompleteEngine;
import co.thefabulous.search.simplesearch.fuzzywuzzy.HabitsSearchEngine;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private ListView listview;
    private ArrayList<Habit> habits;
    private HabitsSearchEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listview);
        habits = new ArrayList<>();
        habits.add(new Habit("Clench your Fists or Squeeze your Hands", "Strengthen your Self-Control"));
        habits.add(new Habit("Break the Habit! The No Sugar Challenge", "Say Goodbye to Sugar"));
        habits.add(new Habit("Create a Timeline", "Transform your to-do list into a game plan"));
        habits.add(new Habit("Eat Whole Grains", "Carbohydrates energize your body"));
        habits.add(new Habit("Eat Fish and Seafood or Flax Seeds", "Omega-3 benefits your heart health"));
        habits.add(new Habit("Block Distractions", "Silence any distractions "));
        habits.add(new Habit("Focused Work", "Focus entirely on the current task"));
        habits.add(new Habit("Adjust & Review Plans", "Review and recalibrate your agenda"));
        habits.add(new Habit("What are my 3 most important tasks?", "Start with these to maximize your efficiency "));
        habits.add(new Habit("I feel great today!", "Today is going to be a great day!"));
        habits.add(new Habit("Darker, Quieter, Cooler", "Create the perfect sleep environment"));
        habits.add(new Habit("Celebrate!", "Celebrate your victories "));
        habits.add(new Habit("Power Nap", "Wake up refreshed!"));

        engine = new HabitsSearchEngine();
        engine.setHabits(habits);

        EditText searchEditText = (EditText) findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(charSequence.toString());
            }
        });

        search("");
    }

    private void search(String s) {
        if (s != null) {
            engine.search(s)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            habits -> listview.setAdapter(new HabitAdapter(this, habits)),
                            error -> Log.e(TAG, error.getMessage())
                    );
        }
    }

}
