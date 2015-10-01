package com.example.anbo.todotesting.SqlInterface;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anbo on 9/25/2015.
 */
public class TodoSqlHelper extends SQLiteOpenHelper {

    public TodoSqlHelper(Context context){
        super(context, TodoContract.DATABASE_NAME, null, TodoContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoContract.CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TodoContract.DROP_TODO_TABLE);
        onCreate(db);
    }
}
