package com.example.anbo.financetesting.localFileStructure;

import com.example.anbo.financetesting.dbInterface.Entry;
import com.example.anbo.financetesting.dbInterface.EntryCollection;
import com.example.anbo.financetesting.dbInterface.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Anbo on 9/2/2015.
 */
public class LocalEntryCollection implements EntryCollection {

    protected List<Entry> entries;

    public LocalEntryCollection(){
        this(new ArrayList<Entry>());
    }

    public LocalEntryCollection(List<Entry> entries){
        this.entries = entries;
    }

    @Override
    public Entry get(int i) {
        return entries.get(i);
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public EntryCollection getEntriesByDate(Date start, Date end) {
        List<Entry> entries = new ArrayList<>();

        for (Entry current : this.entries) {
            Date currentDate = current.getDate();
            if (currentDate.compareTo(start) >= 0 && currentDate.compareTo(end) <= 0)
                entries.add(current);
        }
        return new LocalEntryCollection(entries);
    }

    @Override
    public EntryCollection getEntriesByTag(Tag tag){
        return getEntriesByTag(tag.getTagName());
    }

    @Override
    public EntryCollection getEntriesByTag(String tagName) {
        List<Entry> entries = new ArrayList<>();

        String processedTag = tagName.toLowerCase().trim();

        for (Entry current : this.entries) {
            List<String> currentStrings = current.getTagsAsStringList();
            for (String s : currentStrings) {
                if (s.toLowerCase().trim().equals(processedTag))
                    entries.add(current);
            }
        }
        return new LocalEntryCollection(entries);
    }

    @Override
    public EntryCollection getEntriesByCostRange(double lowerBound, double upperBound) {
        List<Entry> entries = new ArrayList<>();

        for (Entry current : this.entries) {
            double currentCost = current.getCost();
            if (currentCost >= lowerBound && currentCost <= upperBound)
                entries.add(current);
        }
        return new LocalEntryCollection(entries);
    }

    @Override
    public boolean hasTag(String tagName) {
        for (Entry entry : entries) {
            if (entry.hasTag(tagName)) return true;
        }
        return false;
    }

    @Override
    public boolean hasTag(Tag tag) {
        return hasTag(tag.getTagName());
    }

    @Override
    public double getTotal() {
        double total = 0;
        for (Entry current : entries){
            total += current.getCost();
        }
        return total;
    }

    @Override
    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    @Override
    public EntryCollection getEntriesByDate(Date date) {
        List<Entry> entries = new ArrayList<>();

        for (Entry current : this.entries) {
            Date currentDate = current.getDate();
            if (currentDate.compareTo(date) == 0)
                entries.add(current);
        }
        return new LocalEntryCollection(entries);
    }
}
