package co.thefabulous.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import co.thefabulous.search.fuse.Engine;
import co.thefabulous.search.search.AutocompleteEngine;


public class MainActivity extends AppCompatActivity {

    private ListView listview;
    private ArrayList<Habit> habits;
    private Engine<Habit> engine;

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

        listview.setAdapter(new HabitAdapter(this, habits));

        engine = new AutocompleteEngine.Builder<Habit>()
                .setIndex(new SampleAdapter())
                .setAnalyzer(new SampleAnalyzer())
                .build();
        engine.addAll(habits);

        EditText searchEditText = (EditText) findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void search(String s) {
        if (s == null || s.length() == 0) {
            listview.setAdapter(new HabitAdapter(this, habits));
        } else {
            listview.setAdapter(new HabitAdapter(this, engine.search(s)));
        }
    }
}
