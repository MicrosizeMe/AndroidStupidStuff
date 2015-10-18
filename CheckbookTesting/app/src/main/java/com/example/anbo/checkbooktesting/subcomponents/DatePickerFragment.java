package com.example.anbo.checkbooktesting.subcomponents;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import com.example.anbo.checkbooktesting.CostRecordingActivity;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface DatePickerDialogueListener{
        void setDate(int year, int month, int day, boolean isSearch, boolean isLower);
    }

    private boolean isSearch = false;
    private boolean isLower = false;

    DatePickerDialogueListener activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (DatePickerDialogueListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement DatePickerDialogueListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = ((CostRecordingActivity) getActivity()).getEntryDateReading();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        activity.setDate(year, month, day, isSearch, isLower);
    }

    public void setSearch(boolean isSearch, boolean isLower) {
        this.isSearch = isSearch;
        this.isLower = this.isSearch && isLower;
    }
}