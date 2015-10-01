package com.example.anbo.financetesting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.anbo.financetesting.dbInterface.Checkbook;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CostRecordingScreen extends Activity {

    Checkbook checkbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_recording_screen);
        //Intialize checkbook;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cost_recording_screen, menu);
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

    public void clearFields(){
        EditText costBox = (EditText) findViewById(R.id.cost_recording_screen_cost_box);
        costBox.setText("");
        EditText tagBox = (EditText) findViewById(R.id.cost_recording_screen_tags_prompt);
        costBox.setText("");
    }

    public void addEntry(View view){
        EditText costBox = (EditText) findViewById(R.id.cost_recording_screen_cost_box);
        double cost = Double.parseDouble(costBox.getText().toString());

        EditText tagBox = (EditText) findViewById(R.id.cost_recording_screen_tags_prompt);
        char[] tagString = costBox.getText().toString().toLowerCase().trim().toCharArray();
        List<String> stringList = new ArrayList<>();
        String currentString = "";
        for (int i = 0; i < tagString.length; i++) {
            if (tagString[i] == ',') {
                stringList.add(currentString.trim());
                currentString = "";
            }
            else currentString += tagString[i];
        }

        //TODO checkbook function
        //checkbook.createEntry(double cost, List<String> tagNames)
        //Should create the entry and add it as new entry
        checkbook.createEntry(cost, stringList);

        clearFields();
    }

    public void startLookup(View view){
        Intent intent = new Intent(this, EntryLookupScreen.class);
        startActivity(intent);
    }
}
