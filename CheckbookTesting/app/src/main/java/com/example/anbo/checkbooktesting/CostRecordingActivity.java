package com.example.anbo.checkbooktesting;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anbo.checkbooktesting.activityPrototypes.StaticCheckbookActivity;
import com.example.anbo.checkbooktesting.subcomponents.AddTagFragment;
import com.example.anbo.checkbooktesting.subcomponents.DatePickerFragment;
import com.example.anbo.checkbooktesting.subcomponents.TagListAdapter;

import java.util.Calendar;
import java.util.List;

public class CostRecordingActivity extends StaticCheckbookActivity
implements DatePickerFragment.DatePickerDialogueListener, AddTagFragment.AddTagFragmentListener{

    Calendar entryDateReading = StaticUtil.roundDate(Calendar.getInstance());
    TagListAdapter adapter;
    String uuid;

    public static final String EDIT_FLAG = "com.example.CostRecordingActivity.isEditing";

    private class UserInputDataPacket{
        double cost;
        List<String> tagList;
        String note;

        UserInputDataPacket(){}
    }

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

        LinearLayout buttonSpace =
                (LinearLayout) findViewById(R.id.cost_recording_activity_button_space);

        //Not editing, creating
        if (uuid == null) {
            Button button = new Button(this);
            button.setText(R.string.cost_recording_activity_add_entry_button_text);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addEntry(v);
                }
            });
            buttonSpace.addView(button);
        }
        else { //Editing an existing entry
            //TODO do edit logic
            Button deleteEntryButton = new Button(this);
            deleteEntryButton.setText(R.string.cost_recording_activity_delete_entry_button_text);
            deleteEntryButton.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            Button editEntryButton = new Button(this);
            editEntryButton.setText(R.string.cost_recording_activity_edit_entry_button_text);
            editEntryButton.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            buttonSpace.addView(deleteEntryButton);
            buttonSpace.addView(editEntryButton);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cost_recording, menu);
        return true;
    }

    private UserInputDataPacket readUserInfo(){
        //Costs
        EditText costView = (EditText) findViewById(R.id.cost_recording_activity_cost_edit_view);
        String costString;
        double cost;
        try{
            costString = costView.getText().toString();
            cost = Double.parseDouble(costString);
        }
        catch (NumberFormatException e) {
            Toast toast = Toast.makeText(this, "Try to actually type numbers, dumbass."
                    , Toast.LENGTH_LONG);
            toast.show();
            costView.getText().clear();
            return null;
        }
        if (costString.isEmpty()) {
            Toast toast = Toast.makeText(this, "You gotta enter a cost.", Toast.LENGTH_LONG);
            toast.show();
            return null;
        }

        //Note
        EditText noteView = (EditText) findViewById(R.id.cost_recording_screen_note_edit_view);
        String note = noteView.getText().toString();

        //List of tags
        List<String> tagList = adapter.getAllStrings();
        if (tagList.isEmpty()) {
            Toast toast = Toast.makeText(this, "You gotta enter a tag.", Toast.LENGTH_LONG);
            toast.show();
            return null;
        }

        UserInputDataPacket packet = new UserInputDataPacket();
        packet.cost = cost;
        packet.note = note;
        packet.tagList = tagList;
        return packet;
    }

    public void addEntry(View view) {
        UserInputDataPacket packet = readUserInfo();
        if (packet == null) return;

        serviceManager.service.createEntry(
                entryDateReading, packet.cost, packet.tagList, packet.note);
        Toast toast = Toast.makeText(this, "Entry created!", Toast.LENGTH_SHORT);
        toast.show();
        resetFields();
    }

    public void editEntry(View view) {
        UserInputDataPacket packet = readUserInfo();
        if (packet == null) return;

        Toast toast = Toast.makeText(this, "TestText" + uuid, Toast.LENGTH_LONG);
        toast.show();
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

    public void resetFields(){
        entryDateReading = StaticUtil.roundDate(Calendar.getInstance());
        setDateText(entryDateReading);
        adapter.clear();
        EditText costView = (EditText) findViewById(R.id.cost_recording_activity_cost_edit_view);
        costView.setText("");
        EditText noteView = (EditText) findViewById(R.id.cost_recording_screen_note_edit_view);
        noteView.setText("");
    }

    public Calendar getEntryDateReading(boolean isLower){
        return entryDateReading;
    }
}
