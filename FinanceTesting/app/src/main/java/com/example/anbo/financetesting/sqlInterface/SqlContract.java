package com.example.anbo.financetesting.sqlInterface;

/**
 * Created by Anbo on 9/18/2015.
 */
public class SqlContract {
    public SqlContract(){}

    public static final String DATABASE_NAME = "Checkbook.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_ENTRY_TABLE = "CREATE TABLE " + Entry.TABLE_NAME
            + " ("
            + Entry.ID + " INTEGER PRIMARY KEY, "
            + Entry.COST + " REAL, "
            + Entry.DATE + " DATE);";
    public static final String CREATE_TAG_TABLE = "CREATE TABLE " + Tag.TABLE_NAME
            + " ("
            + Tag.NAME + " TEXT PRIMARY KEY);";
    public static final String CREATE_ENTRY_TAG_TABLE = "CREATE TABLE " + EntryTag.TABLE_NAME
            + " ("
            + EntryTag.ENTRY_ID + " INTEGER FOREIGN KEY, "
            + EntryTag.TAG_NAME + " TEXT FOREIGN KEY);";

    public static abstract class Entry {
        public static final String TABLE_NAME = "entries";
        public static final String ID = "entryID";
        public static final String COST = "entryCost";
        public static final String DATE = "entryDate";
    }

    public static abstract class Tag {
        public static final String TABLE_NAME = "tags";
        public static final String NAME = "tagName";
    }

    public static abstract class EntryTag {
        public static final String TABLE_NAME = "entryTagRelations";
        public static final String ENTRY_ID = Entry.ID;
        public static final String TAG_NAME = Tag.NAME;
    }
}

/*
package com.example.anbo.financetesting.sqlInterface;

public class SqlContract {
    public SqlContract(){}

    public static final String DATABASE_NAME = "Checkbook.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_ENTRY_TABLE = "CREATE TABLE " + Entry.TABLE_NAME
            + " ("
            + Entry.ID + " INTEGER PRIMARY KEY, "
            + Entry.COST + " REAL);";
    public static final String CREATE_TAG_TABLE = "CREATE TABLE " + Tag.TABLE_NAME
            + " ("
            + Tag.NAME + " TEXT PRIMARY KEY);";
    public static final String CREATE_DATE_TABLE = "CREATE TABLE " + Date.TABLE_NAME
            + " (" + Date.DATE + " DATE PRIMARY KEY);";
    public static final String CREATE_ENTRY_TAG_TABLE = "CREATE TABLE " + EntryTag.TABLE_NAME
            + " ("
            + EntryTag.ENTRY_ID + " INTEGER FOREIGN KEY, "
            + EntryTag.TAG_NAME + " TEXT FOREIGN KEY);";
    public static final String CREATE_ENTRY_DATE_TABLE = "CREATE TABLE " + EntryDate.TABLE_NAME
            + " ("
            + EntryDate.ENTRY_ID + " INTEGER FOREIGN KEY, "
            + EntryDate.DATE_DATE + " DATE FOREIGN KEY);";



    public static abstract class Entry {
        public static final String TABLE_NAME = "entries";
        public static final String ID = "entryID";
        public static final String COST = "entryCost";
    }

    public static abstract class Tag {
        public static final String TABLE_NAME = "tags";
        public static final String NAME = "tagName";
    }

    public static abstract class Date {
        public static final String TABLE_NAME = "dates";
        public static final String DATE = "dateDate";
    }

    public static abstract class EntryTag {
        public static final String TABLE_NAME = "entryTagRelations";
        public static final String ENTRY_ID = Entry.ID;
        public static final String TAG_NAME = Tag.NAME;
    }

    public static abstract class EntryDate {
        public static final String TABLE_NAME = "entryDateRelations";
        public static final String ENTRY_ID = Entry.ID;
        public static final String DATE_DATE = Date.DATE;
    }
}*/
