package com.example.anbo.checkbooktesting;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anbo.checkbooktesting.subcomponents.AddTagFragment;
import com.example.anbo.checkbooktesting.subcomponents.DatePickerFragment;
import com.example.anbo.checkbooktesting.subcomponents.TagListAdapter;

import java.util.Calendar;
import java.util.List;

public class CostRecordingActivity extends BaseCheckbookActivity
implements DatePickerFragment.DatePickerDialogueListener, AddTagFragment.AddTagFragmentListener{

    Calendar entryDateReading = StaticUtil.roundDate(Calendar.getInstance());
    TagListAdapter adapter;
    String uuid;

    public static final String EDIT_FLAG = "com.example.CostRecordingActivity.isEditing";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_recording);
        setDateText(entryDateReading);
        adapter = new TagListAdapter(this);
        ((ListView) findViewById(R.id.cost_recording_screen_tag_list)).setAdapter(
                adapter);
        Intent intent = getIntent();
        uuid = intent.getStringExtra(EDIT_FLAG);

        //TODO do edit logic
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cost_recording, menu);
        return true;
    }

    public void addEntry(View view) {

        //Costs
        EditText costView = (EditText) findViewById(R.id.cost_recording_activity_cost_edit_view);
        String costString;
        try{
            costString = costView.getText().toString();
        }
        catch (NumberFormatException e) {
            Toast toast = Toast.makeText(this, "Try to actually type numbers, dumbass."
                    , Toast.LENGTH_LONG);
            toast.show();
            costView.getText().clear();
            return;
        }
        if (costString.isEmpty()) {
            Toast toast = Toast.makeText(this, "You gotta enter a cost.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        double cost = Double.parseDouble(costString);

        //Note
        EditText noteView = (EditText) findViewById(R.id.cost_recording_screen_note_edit_view);
        String note = noteView.getText().toString();

        //List of tags
        List<String> tagList = adapter.getAllStrings();
        if (tagList.isEmpty()) {
            Toast toast = Toast.makeText(this, "You gotta enter a tag.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        serviceManager.service.createEntry(entryDateReading, cost, tagList, note);
    }

    @Override
    public String[] getTagStrings() {
        return serviceManager.service.getTagList();
    }

    @Override
    public void receiveString(String string) {
        adapter.add(string);
    }

    public void showTagEntryDialog(View view) {
        DialogFragment fragment = new AddTagFragment();
        fragment.show(getFragmentManager(), "tagAdder");
    }

    public void showDatePickerDialog (View view){
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "datePicker");
    }

    public void setDateText(Calendar date) {
        TextView textView = (TextView) findViewById(R.id.cost_recording_activity_date_text_view);
        textView.setText(StaticUtil.getStringFromCalendar(date));
    }

    public void setDate (int year, int month, int day, boolean isSearch, boolean isLower){
        //don't care about lower or search
        entryDateReading.set(Calendar.YEAR, year);
        entryDateReading.set(Calendar.MONTH, month);
        entryDateReading.set(Calendar.DAY_OF_MONTH, day);
        setDateText(entryDateReading);
    }

    public Calendar getEntryDateReading(boolean isLower){
        return entryDateReading;
    }
}
