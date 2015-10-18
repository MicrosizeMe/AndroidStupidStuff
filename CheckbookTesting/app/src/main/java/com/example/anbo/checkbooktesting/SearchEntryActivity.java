package com.example.anbo.checkbooktesting;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anbo.checkbooktesting.subcomponents.AddTagFragment;
import com.example.anbo.checkbooktesting.subcomponents.DatePickerFragment;
import com.example.anbo.checkbooktesting.subcomponents.TagListAdapter;

import java.util.Calendar;

public class SearchEntryActivity extends BaseCheckbookActivity
implements DatePickerFragment.DatePickerDialogueListener,
        AddTagFragment.AddTagFragmentListener{

    Calendar dateLower = null;
    Calendar dateUpper = null;
    TagListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_entry);
        adapter = new TagListAdapter(this);
        ListView listView = (ListView) findViewById(R.id.search_entry_activity_tag_list_view);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_entry, menu);
        return true;
    }

    public void search(View view) {
        //Todo implement search functionality
    }

    public void showTagEntryDialog(View view) {
        DialogFragment fragment = new AddTagFragment();
        fragment.show(getFragmentManager(), "tagAdder");
    }

    public void showDatePickerDialog (View view){
        DatePickerFragment fragment = new DatePickerFragment();
        boolean isLower = view.getId() == R.id.search_entry_activity_date_low_bound_button;
        fragment.setSearch(true, isLower);
        String tag = isLower ? "datePickerLowerFragment" : "datePickerUpperFragment";
        fragment.show(getFragmentManager(), tag);
    }

    @Override
    public String[] getTagStrings() {
        return serviceManager.service.getTagList();
    }

    @Override
    public void receiveString(String string) {
        adapter.add(string);
    }

    @Override
    public Calendar getEntryDateReading(boolean isLower) {
        if (isLower) {
            if (dateLower == null)
                dateLower = StaticUtil.roundDate(Calendar.getInstance());
            return dateLower;
        }
        if (dateUpper == null)
            dateUpper = StaticUtil.roundDate(Calendar.getInstance());
        return dateUpper;
    }

    @Override
    public void setDate(int year, int month, int day, boolean isSearch, boolean isLower) {
        Calendar currentCal = isLower ? dateLower : dateUpper;
        currentCal.set(Calendar.YEAR, year);
        currentCal.set(Calendar.MONTH, month);
        currentCal.set(Calendar.DAY_OF_MONTH, day);
        setDateText(currentCal, isLower);
    }

    public void setDateText(Calendar date, boolean isLower) {
        int targetView = isLower ?
                R.id.search_entry_activity_date_low_bound_text_view
                : R.id.search_entry_activity_date_high_bound_text_view;
        TextView textView = (TextView) findViewById(targetView);
        textView.setText(StaticUtil.getStringFromCalendar(date));
    }
}
