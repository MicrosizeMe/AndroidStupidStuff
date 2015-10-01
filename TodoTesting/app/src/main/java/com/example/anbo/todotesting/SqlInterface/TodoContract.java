package com.example.anbo.todotesting.SqlInterface;

/**
 * Created by Anbo on 9/25/2015.
 */
public class TodoContract {

    public static final String DATABASE_NAME = "Todo.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_TODO_TABLE = "CREATE TABLE "
            + TodoEntry.TABLE_NAME
            + " ("
            //+ TodoEntry.TODO_ID + " INTEGER PRIMARY ID, "
            + TodoEntry.COMPLETED + " INTEGER, "
            + TodoEntry.DESCRIPTION + " TEXT);";

    public static final String DROP_TODO_TABLE = "DROP TABLE IF EXISTS "
            + TodoEntry.TABLE_NAME + ";";

    public static final class TodoEntry {
        public static final String TABLE_NAME = "todoList";
        public static final String DESCRIPTION = "todoDescription";
        public static final String TODO_ID = "todoID";
        public static final String COMPLETED = "todoCompleted";
    }
}
