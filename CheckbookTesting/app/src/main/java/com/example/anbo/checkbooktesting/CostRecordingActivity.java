package com.example.anbo.checkbooktesting;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

public class CostRecordingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_recording);
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
        EditText costText = (EditText) findViewById(R.id.cost_recording_activity_cost_edit_view);
        double cost = Double.parseDouble(costText.getText().toString().trim());
        EditText dateText = (EditText) findViewById(R.id.cost_recording_activity_date_edit_view);
        String dateString = dateText.getText().toString();

        Calendar date = Calendar.getInstance();

        date.set(Calendar.MILLISECOND, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.HOUR_OF_DAY, 0);

        if (dateString != ""){
            String fragment = "";
            char[] dateArray = dateString.toCharArray();

            //Month
            int count = processNextFragment(dateArray, 0, date, Calendar.MONTH);

            //Day
            count = processNextFragment(dateArray, count, date, Calendar.DAY_OF_MONTH);

            //Year
            processNextFragment(dateArray, count, date, Calendar.YEAR);
        }
    }

    private int processNextFragment(char[] dateArray, int count, Calendar date, int calendarParem) {
        String fragment = "";
        for (int i = count; i < dateArray.length; i++) {
            if (Character.isDigit(dateArray[i]))
                fragment += dateArray[i];
            else {
                date.set(calendarParem, Integer.parseInt(fragment));
                return i + 1; //Skip the encoding symbol we're currently on
            }
        }
        date.set(calendarParem, Integer.parseInt(fragment));
        return dateArray.length;
    }
}
