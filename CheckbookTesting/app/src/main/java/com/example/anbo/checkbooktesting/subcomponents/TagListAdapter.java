package com.example.anbo.checkbooktesting.subcomponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anbo.checkbooktesting.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anbo on 10/14/2015.
 */
public class TagListAdapter extends ArrayAdapter<String>{
    private Context context;

    public TagListAdapter (Context context){
        super(context, -1, new ArrayList<String>());
        this.context = context;
    }

    public List<String> getAllStrings() {
        int size = getCount();
        List<String> returnList = new ArrayList<>();
        for (int i = 0; i < size; i++){
            String item = getItem(i);
            if (item != null && !item.isEmpty());
                returnList.add(getItem(i));
        }
        return returnList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.tag_entry_or_search_term, null, false);
        TextView textView = (TextView) layout.findViewById(R.id.tag_entry_or_search_text_view);
        textView.setText(getItem(position));

        ImageButton button =
                (ImageButton) layout.findViewById(R.id.tag_entry_or_search_delete_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
            }
        });
        return layout;
    }
}
