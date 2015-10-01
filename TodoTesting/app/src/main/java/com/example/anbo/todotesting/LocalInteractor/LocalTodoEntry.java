package com.example.anbo.todotesting.LocalInteractor;

import com.example.anbo.todotesting.DbInteractor.TodoEntry;

/**
 * Created by Anbo on 9/25/2015.
 */
public class LocalTodoEntry implements TodoEntry {
    private String description;
    private boolean isCompleted = false;

    public LocalTodoEntry(String description){
        this.description = description;
    }

    public LocalTodoEntry(String description, boolean isCompleted){
        this(description);
        this.isCompleted = isCompleted;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }
}
