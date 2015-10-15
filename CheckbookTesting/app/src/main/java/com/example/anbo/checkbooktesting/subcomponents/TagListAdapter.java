package com.example.anbo.checkbooktesting.subcomponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.example.anbo.checkbooktesting.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anbo on 10/14/2015.
 */
public class TagListAdapter extends BaseAdapter{
    private String[] tagStrings;
    private List<View> views = new ArrayList<>();
    private Context context;
    private boolean autoUpdates = true;

    public TagListAdapter (Context context, String[] tagStrings){
        this.context = context;
        this.tagStrings = tagStrings;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public String getItem(int position) {
        AutoCompleteTextView textView =
                (AutoCompleteTextView)
                        views.get(position).findViewById(R.id.tag_entry_or_search_text_view);
        return textView.getText().toString().trim().toLowerCase();
    }

    public List<String> getAllStrings() {
        int size = views.size();
        List<String> returnList = new ArrayList<>();
        for (int i = 0; i < size; i++){
            String item = getItem(i);
            if (item != null && !item.isEmpty());
                returnList.add(getItem(i));
        }
        return returnList;
    }

    public View remove(int position){
        View view = views.remove(position);
        if (autoUpdates) notifyDataSetChanged();
        return view;
    }

    public void clear(){
        views.clear();
        if (autoUpdates) notifyDataSetChanged();
    }

    public View addBox(){
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.tag_entry_or_search_term, null, false);
        AutoCompleteTextView textView =
                (AutoCompleteTextView) layout.findViewById(R.id.tag_entry_or_search_text_view);
        textView.setAdapter(new ArrayAdapter<>(context, -1, tagStrings));

        ImageButton button =
                (ImageButton) layout.findViewById(R.id.tag_entry_or_search_delete_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(views.size());
            }
        });
        views.add(layout);
        if (autoUpdates) notifyDataSetChanged();
        return layout;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return views.get(position);
    }

    public void setNotifyOnChange(boolean notifyOnChange){
        this.autoUpdates = notifyOnChange;
    }
}
