package com.example.anbo.todotesting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.anbo.todotesting.DbInteractor.TodoEntry;
import com.example.anbo.todotesting.DbInteractor.TodoList;
import com.example.anbo.todotesting.R;

import java.util.ArrayList;

/**
 * Created by Anbo on 9/25/2015.
 */
public class CheckboxAdapter extends ArrayAdapter<TodoEntry> {
    private TodoList list;

    public CheckboxAdapter(Context context, TodoList list) {
        super(context, -1, new ArrayList<TodoEntry>());
        setNotifyOnChange(true);
        this.list = list;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_view_task, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.todo_list_task_entry_text);
        textView.setText(getItem(position).getDescription());
        CheckBox box = (CheckBox) rowView.findViewById(R.id.todo_list_task_entry_box);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
                list.removeEntry(position);
            }
        });
        return rowView;
    }
}
