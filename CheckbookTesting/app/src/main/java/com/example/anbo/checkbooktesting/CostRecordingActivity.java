package com.example.anbo.checkbooktesting;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anbo.checkbooktesting.activityPrototypes.BaseCheckbookActivity;
import com.example.anbo.checkbooktesting.checkbookInterface.Entry;
import com.example.anbo.checkbooktesting.sqlDBInteractions.CheckbookService;
import com.example.anbo.checkbooktesting.subcomponents.AddTagFragment;
import com.example.anbo.checkbooktesting.subcomponents.DatePickerFragment;
import com.example.anbo.checkbooktesting.subcomponents.DeletionConfirmationFragment;
import com.example.anbo.checkbooktesting.subcomponents.TagListAdapter;

import java.util.Calendar;
import java.util.List;

public class CostRecordingActivity extends BaseCheckbookActivity
implements DatePickerFragment.DatePickerDialogueListener, AddTagFragment.AddTagFragmentListener,
DeletionConfirmationFragment.DeletionConfirmationReceiver{

    Calendar entryDateReading = StaticUtil.roundDate(Calendar.getInstance());
    TagListAdapter adapter;
    String uuid;

    private CheckbookService checkbookService;
    private boolean isBound = false;

    private class CostRecordingServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CheckbookService.CheckbookBinder binder = (CheckbookService.CheckbookBinder) service;
            checkbookService = binder.getService();
            if (uuid != null) {
                //Todo get entry
                Entry entry = checkbookService.getEntry(uuid);
                populateFields(entry);
            }
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    }

    private CostRecordingServiceConnection connection = new CostRecordingServiceConnection();

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
            deleteEntryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment fragment = new DeletionConfirmationFragment();
                    fragment.show(getFragmentManager(), "deleteFragment");
                }
            });
            Button editEntryButton = new Button(this);
            editEntryButton.setText(R.string.cost_recording_activity_edit_entry_button_text);
            editEntryButton.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            editEntryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editEntry(v);
                }
            });


            buttonSpace.addView(deleteEntryButton);
            buttonSpace.addView(editEntryButton);
            //Todo get info

            //Entry entry;
            //populateFields(entry);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isBound) {
            Intent intent = new Intent(this, CheckbookService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cost_recording, menu);
        return true;
    }

    private void populateFields(Entry data){
        ((EditText) findViewById(R.id.cost_recording_activity_cost_edit_view))
                .setText(StaticUtil.parseDouble(data.getCost()));
        if (data.getNote() != null)
            ((EditText) findViewById(R.id.cost_recording_screen_note_edit_view))
                    .setText(data.getNote());
        adapter.addAll(data.getTags());
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

        checkbookService.createEntry(
                entryDateReading, packet.cost, packet.tagList, packet.note);
        Toast toast = Toast.makeText(this, "Entry created!", Toast.LENGTH_SHORT);
        toast.show();
        resetFields();
    }

    public void editEntry(View view) {
        UserInputDataPacket packet = readUserInfo();
        if (packet == null) return;

        //TODO finish this
        Toast toast = Toast.makeText(this, "TestText" + uuid, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public String[] getTagStrings() {
        return checkbookService.getTagList();
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

    @Override
    public void onDeletionConfirm() {
        checkbookService.deleteEntry(uuid);
        Toast toast = Toast.makeText(this, "Entry deleted. You monster.", Toast.LENGTH_LONG);
        toast.show();
        finish();
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
