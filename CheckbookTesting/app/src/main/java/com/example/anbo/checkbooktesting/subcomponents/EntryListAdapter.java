package com.example.anbo.checkbooktesting.subcomponents;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anbo.checkbooktesting.CostRecordingActivity;
import com.example.anbo.checkbooktesting.R;
import com.example.anbo.checkbooktesting.StaticUtil;
import com.example.anbo.checkbooktesting.checkbookInterface.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anbo on 10/18/2015.
 */
public class EntryListAdapter extends ArrayAdapter<Entry> {
    public EntryListAdapter(Context context) {
        super(context, -1, new ArrayList<Entry>());
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.entry_lookup_view, null);
        TextView dateView = (TextView) layout.findViewById(R.id.entry_lookup_view_date_text_view);
        TextView costView = (TextView) layout.findViewById(R.id.entry_lookup_view_cost_text_view);
        TextView tagsView = (TextView) layout.findViewById(R.id.entry_lookup_view_tags_view);
        TextView noteView = (TextView) layout.findViewById(R.id.entry_lookup_view_note_view);

        final Entry currentEntry = getItem(position);
        dateView.setText(StaticUtil.getStringFromCalendar(currentEntry.getDate()));
        costView.setText("" + currentEntry.getCost());
        String note = currentEntry.getNote();
        if (note != null) noteView.setText(note);

        List<String> tags = currentEntry.getTags();
        String tagList = "";
        for (String string : tags) {
            tagList += string;
        }
        tagsView.setText(tagList);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Modify CostRecordingActivity to be able to modify costs
                Intent intent = new Intent(getContext(), CostRecordingActivity.class);
                intent.putExtra(CostRecordingActivity.EDIT_FLAG, currentEntry.getUUID().toString());
            }
        });

        return layout;
    }
}
