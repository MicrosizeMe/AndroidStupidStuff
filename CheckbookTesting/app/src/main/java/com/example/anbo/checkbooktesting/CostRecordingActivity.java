package com.example.anbo.checkbooktesting;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.anbo.checkbooktesting.SqlDBInteractions.CheckbookService;

import java.util.Calendar;

public class CostRecordingActivity extends Activity {

    CheckbookServiceManager serviceManager = new CheckbookServiceManager();
    Calendar entryDateReading = roundDate(Calendar.getInstance());
    CheckbookService checkbook;

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
        checkbook = serviceManager.service;
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
        ((CheckbookService) checkbook).showToast();
    }

    public void showDatePickerDialog (View view){
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "datePicker");
    }

    public void setDateText(Calendar date) {
        TextView textView = (TextView) findViewById(R.id.cost_recording_activity_date_text_view);
        textView.setText(getStringFromCalendar(date));
    }

    public void setDate (int year, int month, int day){
        entryDateReading.set(Calendar.YEAR, year);
        entryDateReading.set(Calendar.MONTH, month);
        entryDateReading.set(Calendar.DAY_OF_MONTH, day);
        setDateText(entryDateReading);
    }

    public static Calendar roundDate(Calendar date) {
        date.set(Calendar.MILLISECOND, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.HOUR_OF_DAY, 0);
        return date;
    }

    public static String getStringFromCalendar(Calendar date){
        return (date.get(Calendar.MONTH) + 1) + "."
                + date.get(Calendar.DAY_OF_MONTH) + "."
                + date.get(Calendar.YEAR);
    }
}
