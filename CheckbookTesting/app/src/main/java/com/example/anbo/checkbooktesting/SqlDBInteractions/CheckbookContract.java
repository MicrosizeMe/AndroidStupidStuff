package com.example.anbo.checkbooktesting.SqlDBInteractions;

import java.util.List;

/**
 * Created by Anbo on 10/5/2015.
 */
public class CheckbookContract {

    public final static String DATABASE_NAME = "checkbookContract.db";
    public final static int DATABASE_VERSION = 1;

    public final static String CREATE_ALL_TABLES =
            ENTRY.CREATE_TABLE + " "
            + TAG.CREATE_TABLE + " "
            + DATE.CREATE_TABLE + " "
            + ENTRY_TO_DATE.CREATE_TABLE + " "
            + ENTRY_TO_TAG.CREATE_TABLE + " "
            + TAG_RULES.CREATE_TABLE;
    public final static String DELETE_ALL_TABLES =
            ENTRY.DROP_TABLE + " "
            + TAG.DROP_TABLE + " "
            + DATE.DROP_TABLE + " "
            + ENTRY_TO_DATE.DROP_TABLE + " "
            + ENTRY_TO_TAG.DROP_TABLE + " "
            + TAG_RULES.DROP_TABLE;



    public abstract static class ENTRY {
        public final static String TABLE_NAME = "entryTable";
        public final static String UUID = "entryUUID";
        public final static String DATE_COLUMN_NAME = "entryDateID";
        public final static String COST_COLUMN_NAME = "entryCost";
        //Tags
        public final static String NOTE_COLUMN_NAME = "entryNote";

        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + UUID + " INTEGER PRIMARY KEY, "
                + DATE_COLUMN_NAME + " INTEGER FOREIGN KEY, "
                + COST_COLUMN_NAME + " REAL, "
                + NOTE_COLUMN_NAME + " TEXT);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    }

    public abstract static class TAG {
        public final static String TABLE_NAME = "tagTable";
        public final static String UUID = "tagUUID";
        public final static String NAME_COLUMN_NAME = "tagName";

        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + UUID + " INTEGER PRIMARY KEY, "
                + NAME_COLUMN_NAME + " TEXT);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public abstract static class DATE {
        public final static String TABLE_NAME = "dateTable";
        public final static String UUID = "dateUUID";
        public final static String DATE_COLUMN_NAME = "dateDate";

        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + UUID + " INTEGER PRIMARY KEY, "
                + DATE_COLUMN_NAME + " DATE);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public abstract static class ENTRY_TO_TAG {
        public final static String TABLE_NAME = "entryToTagTable";
        public final static String ENTRY_COLUMN_NAME = "entryToTagEntry";
        public final static String   TAG_COLUMN_NAME = "entryToTagTag";

        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + ENTRY_COLUMN_NAME + " INTEGER FOREIGN KEY, "
                + TAG_COLUMN_NAME + " INTEGER FOREIGN KEY);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public abstract static class ENTRY_TO_DATE {
        public final static String TABLE_NAME = "entryToDateTable";
        public final static String ENTRY_COLUMN_NAME = "entryToDateEntry";
        public final static String  DATE_COLUMN_NAME = "entryToDateDate";

        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + ENTRY_COLUMN_NAME + " INTEGER FOREIGN KEY, "
                + DATE_COLUMN_NAME + " INTEGER FOREIGN KEY);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public abstract static class TAG_RULES {
        public final static String TABLE_NAME = "tagRulesTable";
        public final static String TAG = "tagRulesTag";
        public final static String TAG_IMPLIES = "tagRulesTagImplies";

        public final static String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + TAG + " INTEGER FOREIGN KEY, "
                + TAG_IMPLIES + " INTEGER FOREIGN KEY);";
        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
}
