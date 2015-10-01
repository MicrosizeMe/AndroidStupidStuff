package com.example.anbo.todotesting;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anbo.todotesting.DbInteractor.TodoEntry;
import com.example.anbo.todotesting.DbInteractor.TodoList;
import com.example.anbo.todotesting.LocalInteractor.LocalTodoEntry;
import com.example.anbo.todotesting.LocalInteractor.LocalTodoList;

import java.util.ArrayList;
import java.util.List;

public class TodoListScreen extends Activity {

    TodoList list;
    ListView listView;
    CheckboxAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_screen);

        list = new LocalTodoList(this);

        listView = (ListView) findViewById(R.id.todo_list_screen_list_view);
        adapter = new CheckboxAdapter(this, list);
        listView.setAdapter(adapter);

        List<TodoEntry> entryList = list.getEntries();

        for (TodoEntry entry : entryList) addItem(entry);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.remove(adapter.getItem(position));
                list.removeEntry(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list_screen, menu);
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

    public void addNewEntry(View view) {
        EditText text = (EditText) findViewById(R.id.todo_list_screen_edit_text);
        String description = text.getText().toString();
        TodoEntry entry = new LocalTodoEntry(description);
        list.addEntry(entry);
        addItem(entry);
    }

    public void addItem(TodoEntry entry){
        /*LayoutInflater inflater = getLayoutInflater();
        ViewGroup box = (ViewGroup) inflater.inflate(R.layout.activity_todo_list_screen,
                null);
        TextView textView = (TextView) box.findViewById(R.id.todo_list_task_entry_text);
        textView.setText(entry.getDescription());*/
        adapter.add(entry);
    }

    public void removeItem(TodoEntry entry){
        adapter.remove(entry);
    }
}
