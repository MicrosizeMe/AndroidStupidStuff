package com.example.anbo.todotesting.LocalInteractor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.anbo.todotesting.DbInteractor.TodoEntry;
import com.example.anbo.todotesting.DbInteractor.TodoList;
import com.example.anbo.todotesting.SqlInterface.TodoContract;
import com.example.anbo.todotesting.SqlInterface.TodoSqlHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anbo on 9/25/2015.
 */
public class LocalTodoList implements TodoList{
    private List<TodoEntry> entryList = new ArrayList<>();
    private SQLiteDatabase db ;

    String[] columns = {TodoContract.TodoEntry.COMPLETED, TodoContract.TodoEntry.DESCRIPTION};

    public LocalTodoList(Context context){
        db = new TodoSqlHelper(context).getWritableDatabase();
        //db = context.openOrCreateDatabase(TodoContract.DATABASE_NAME, Context.MODE_PRIVATE, null);
        Cursor allEntries = db.query(true, TodoContract.TodoEntry.TABLE_NAME,
                columns,
                TodoContract.TodoEntry.COMPLETED + " = 0", null, null, null, null, null);
        allEntries.moveToFirst();
        int size = allEntries.getCount();
        for (int i = 0; i < size; i++) {
            boolean completed = allEntries.getInt(
                    allEntries.getColumnIndex(TodoContract.TodoEntry.COMPLETED)) == 1;
            String description = allEntries.getString(
                    allEntries.getColumnIndex(TodoContract.TodoEntry.DESCRIPTION));
            entryList.add(new LocalTodoEntry(description, completed));
            allEntries.move(1);
        }
    }

    @Override
    public void addEntry(TodoEntry entry) {
        entryList.add(entry);
        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COMPLETED, entry.isCompleted());
        values.put(TodoContract.TodoEntry.DESCRIPTION, entry.getDescription());
        db.insert(TodoContract.TodoEntry.TABLE_NAME,
                null, values);
    }

    @Override
    public void removeEntry(int index) {
        TodoEntry entry = entryList.get(index);
        entryList.remove(index);
    }

    @Override
    public List<TodoEntry> getEntries() {
        return entryList;
    }
}
