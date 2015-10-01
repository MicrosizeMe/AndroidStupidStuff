package com.example.anbo.financetesting.sqlInterface;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anbo on 9/18/2015.
 */
public class SqlConnectionHelper extends SQLiteOpenHelper {
    public SqlConnectionHelper(Context context) {
        super(context, SqlContract.DATABASE_NAME, null, SqlContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlContract.CREATE_ENTRY_TABLE);
        //db.execSQL(SqlContract.CREATE_DATE_TABLE);
        db.execSQL(SqlContract.CREATE_TAG_TABLE);
        //db.execSQL(SqlContract.CREATE_ENTRY_DATE_TABLE);
        db.execSQL(SqlContract.CREATE_ENTRY_TAG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Do nothing
    }
}
