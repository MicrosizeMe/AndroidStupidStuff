package com.example.anbo.checkbooktesting.sqlDBInteractions;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anbo on 10/9/2015.
 */
public class CheckbookSqlHelper extends SQLiteOpenHelper {
    public CheckbookSqlHelper(Context context){
        super(context, CheckbookContract.DATABASE_NAME, null, CheckbookContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CheckbookContract.DATE.CREATE_TABLE);
        assert (true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void resetDb(SQLiteDatabase db) {
        CheckbookContract.dropAllTables(db);
        CheckbookContract.createAllTables(db);
    }
}
