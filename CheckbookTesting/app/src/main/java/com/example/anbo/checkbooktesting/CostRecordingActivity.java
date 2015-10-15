package com.example.anbo.checkbooktesting;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anbo.checkbooktesting.sqlDBInteractions.CheckbookContract;
import com.example.anbo.checkbooktesting.sqlDBInteractions.CheckbookService;
import com.example.anbo.checkbooktesting.subcomponents.DatePickerFragment;
import com.example.anbo.checkbooktesting.subcomponents.TagListAdapter;

import java.util.Calendar;

public class CostRecordingActivity extends Activity {

    CheckbookServiceManager serviceManager = new CheckbookServiceManager();
    Calendar entryDateReading = StaticUtil.roundDate(Calendar.getInstance());
    TagListAdapter adapter;
    boolean adapterInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_recording);
        setDateText(entryDateReading);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, CheckbookService.class);
        bindService(intent, serviceManager.mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceManager.isBound) {
            unbindService(serviceManager.mConnection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cost_recording, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addEntry(View view) {
        //checkbook.showToast();
        /*double cost =
                Double.parseDouble(
                        ((EditText) findViewById(R.id.cost_recording_activity_cost_edit_view))
                                .getText().toString().trim()
                );
        //cost = Math.floor(cost * 100) / 100;*/
        long cost = StaticUtil.getMinutesSinceEpoch(entryDateReading);
        //String valid = Boolean.toString(serviceManager.service == null);
        Toast toast = Toast.makeText(this, "" + cost, Toast.LENGTH_LONG);
        toast.show();
    }

    public void addTag(View view) {
        if (!adapterInitialized){
            //TODO Figure out how this is actually supposed to work
            adapter = new TagListAdapter(this, serviceManager.service.getTagList());
            ((ListView) findViewById(R.id.cost_recording_screen_tag_list)).setAdapter(adapter);
            adapterInitialized = true;
        }
        adapter.addBox();
    }

    public void showDatePickerDialog (View view){
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "datePicker");
    }

    public void setDateText(Calendar date) {
        TextView textView = (TextView) findViewById(R.id.cost_recording_activity_date_text_view);
        textView.setText(StaticUtil.getStringFromCalendar(date));
    }

    public void setDate (int year, int month, int day){
        entryDateReading.set(Calendar.YEAR, year);
        entryDateReading.set(Calendar.MONTH, month);
        entryDateReading.set(Calendar.DAY_OF_MONTH, day);
        setDateText(entryDateReading);
    }

    public Calendar getEntryDateReading(){
        return entryDateReading;
    }
}
