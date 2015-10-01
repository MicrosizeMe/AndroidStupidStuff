package com.example.anbo.todotesting.DbInteractor;

import java.util.List;

/**
 * Created by Anbo on 9/24/2015.
 */
public interface TodoList {
    List<TodoEntry> getEntries();

    void addEntry(TodoEntry entry);

    void removeEntry(int index);
}
