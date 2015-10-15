package com.example.anbo.checkbooktesting.sqlDBInteractions;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Anbo on 10/5/2015.
 */
public class CheckbookContract {

    public final static String DATABASE_NAME = "checkbookContract.db";
    public final static int DATABASE_VERSION = 1;

    public static void createAllTables(SQLiteDatabase db){
        db.execSQL(ENTRY.CREATE_TABLE);
        db.execSQL(TAG.CREATE_TABLE);
        db.execSQL(DATE.CREATE_TABLE);
        db.execSQL(ENTRY_TO_TAG.CREATE_TABLE);
        db.execSQL(TAG_RULES.CREATE_TABLE);
    }

    public static void dropAllTables(SQLiteDatabase db){
        db.execSQL(ENTRY.DROP_TABLE);
        db.execSQL(TAG.DROP_TABLE);
        db.execSQL(DATE.DROP_TABLE);
        db.execSQL(ENTRY_TO_TAG.DROP_TABLE);
        db.execSQL(TAG_RULES.DROP_TABLE);
    }

    public abstract static class ENTRY {
        public final static String TABLE_NAME = "entryTable";
        public final static String UUID = "entryUUID";
        public final static int UUID_COLUMN_INDEX = 0;
        public final static String DATE_UUID_COLUMN_NAME = "entryDateID";
        public final static int DATE_UUID_COLUMN_INDEX = 1;
        public final static String COST_COLUMN_NAME = "entryCost";
        public final static int COST_COLUMN_INDEX = 2;
        //Tags
        public final static String NOTE_COLUMN_NAME = "entryNote";
        public final static int NOTE_COLUMN_INDEX = 3;
        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + UUID + " TEXT PRIMARY KEY, "
                + DATE_UUID_COLUMN_NAME + " TEXT, " //" INTEGER FOREIGN KEY, "
                + COST_COLUMN_NAME + " REAL, "
                + NOTE_COLUMN_NAME + " TEXT);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    }

    public abstract static class TAG {
        public final static String TABLE_NAME = "tagTable";
        public final static String UUID = "tagUUID";
        public final static int UUID_COLUMN_INDEX = 0;
        public final static String NAME_COLUMN_NAME = "tagName";
        public final static int NAME_COLUMN_INDEX = 1;

        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + UUID + " TEXT PRIMARY KEY, "
                + NAME_COLUMN_NAME + " TEXT);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public abstract static class DATE {
        public final static String TABLE_NAME = "dateTable";
        public final static String UUID = "dateUUID";
        public final static int UUID_COLUMN_INDEX = 0;
        public final static String DATE_COLUMN_NAME = "dateDate";
        public final static int DATE_COLUMN_INDEX = 1;

        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + UUID + " TEXT PRIMARY KEY, "
                + DATE_COLUMN_NAME + " INTEGER);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public abstract static class ENTRY_TO_TAG {
        public final static String TABLE_NAME = "entryToTagTable";
        public final static String ENTRY_UUID_COLUMN_NAME = "entryToTagEntry";
        public final static int ENTRY_UUID_COLUMN_INDEX = 0;
        public final static String TAG_UUID_COLUMN_NAME = "entryToTagTag";
        public final static int TAG_UUID_COLUMN_INDEX = 1;

        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + ENTRY_UUID_COLUMN_NAME + " TEXT, "//" INTEGER FOREIGN KEY, "
                + TAG_UUID_COLUMN_NAME + " TEXT);";//" INTEGER FOREIGN KEY);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    /*public abstract static class ENTRY_TO_DATE {
        public final static String TABLE_NAME = "entryToDateTable";
        public final static String ENTRY_COLUMN_NAME = "entryToDateEntry";
        public final static String  DATE_UUID_COLUMN_NAME = "entryToDateDate";

        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + ENTRY_COLUMN_NAME + " INTEGER FOREIGN KEY, "
                + DATE_UUID_COLUMN_NAME + " INTEGER FOREIGN KEY);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }*/

    public abstract static class TAG_RULES {
        public final static String TABLE_NAME = "tagRulesTable";
        public final static String TAG = "tagRulesTag";
        public final static int TAG_COLUMN_INDEX = 0;
        public final static String TAG_IMPLIES = "tagRulesTagImplies";
        public final static int TAG_IMPLIES_COLUMN_INDEX = 1;

        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + TAG + " TEXT, "//" INTEGER FOREIGN KEY, "
                + TAG_IMPLIES + " TEXT);";//" INTEGER FOREIGN KEY);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
}
