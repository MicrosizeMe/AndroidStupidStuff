package com.example.anbo.financetesting.dbInterface;

import java.util.Date;

/**
 * Created by Anbo on 9/2/2015.
 */
public interface EntryCollection {
    EntryCollection getEntriesByDate(Date date);

    EntryCollection getEntriesByDate(Date start, Date end);

    EntryCollection getEntriesByTag(String tagName);

    EntryCollection getEntriesByTag(Tag tag);

    EntryCollection getEntriesByCostRange(double lowerBound, double upperBound);

    boolean hasTag(String tagName);

    boolean hasTag(Tag tag);

    Entry get(int i);

    int size();

    double getTotal();

    void addEntry(Entry entry);

}
