package com.example.anbo.financetesting.localFileStructure;

import com.example.anbo.financetesting.dbInterface.Checkbook;
import com.example.anbo.financetesting.dbInterface.Entry;
import com.example.anbo.financetesting.dbInterface.EntryCollection;
import com.example.anbo.financetesting.dbInterface.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anbo on 9/3/2015.
 */
public class LocalCheckbook extends LocalEntryCollection implements Checkbook{

    private HashMap<String, Tag> tagHashMap;
    private HashMap<Date, EntryCollection> dateMap;

    public LocalCheckbook(){
        getEntriesFromFile(null);
    }

    @Override
    public boolean remove(int i) {
        if (entries.size() <= i) return false;
        else entries.remove(i);
        //TODO remove from file;
        //TODO remove from hash structures;
        return true;
    }

    public boolean updateEntry(LocalEntry entry){
        return updateEntry(entry.getID());
    }

    public boolean updateEntry(int entryID){
        //TODO implement update code;

        return true;
    }

    @Override
    public EntryCollection getEntriesByDate(Date date) {
        return dateMap.get(date);
    }

    @Override
    public EntryCollection getEntriesByTag(String tagName) {
        return getTag(tagName);
    }

    @Override
    //TODO entry assumed to be new, this may be buggy
    public void addEntry(Entry entry) {
        super.addEntry(entry);
        EntryCollection dateCollection = getEntriesByDate(entry.getDate());
        if (dateCollection == null){
             //Don't have any entries for this date, make new entry collection
            createDate(entry.getDate()).addEntry(entry);
        }
        else dateCollection.addEntry(entry);

        List<String> tagList = entry.getTagsAsStringList();
        for (String tagName : tagList) {
            Tag tag = getTag(tagName);
            if (tag == null) {
                createTag(tagName).addEntry(entry);
            }
            else tag.addEntry(entry);
        }
    }

    @Override
    public Entry createEntry(double cost, Date date, String... tagNames) {
        //TODO add to file structure
        //TODO add better ID system
        //TODO add file structure manager
        Tag[] tags = new Tag[tagNames.length];
        for (int i = 0; i < tagNames.length; i++) {
            tags[i] = createTag(tagNames[i]);
        }
        Entry newEntry = new LocalEntry(0, cost, this, tags); //TODO fix id here
        addEntry(newEntry);
        return newEntry;
    }

    @Override
    public EntryCollection getEntriesByTag(Tag tag) {
        return tagHashMap.get(tag.getTagName());
    }

    @Override
    public Tag getTag(String tagName) {
        return tagHashMap.get(tagName);
    }

    @Override
    public Tag createTag(String tagName) {
        if (hasTag(tagName)){
            return getTag(tagName);
        }
        Tag newTag = new LocalTag(tagName);
        tagHashMap.put(tagName, newTag);
        return newTag;
    }

    @Override
    public boolean hasTag(String tagName) {
        return tagHashMap.containsKey(tagName);
    }

    public EntryCollection createDate(Date date){
        if (hasDate(date)) return getEntriesByDate(date);
        EntryCollection newDateCollection = new LocalEntryCollection();
        dateMap.put(date, newDateCollection);
        return newDateCollection;
    }

    public boolean hasDate(Date date){
        return dateMap.containsKey(date);
    }

    public List<Entry> getEntriesFromFile(String file){
        List<Entry> entries = new ArrayList<>();
        this.entries = entries;

        for (Entry entry : entries){
            List<String> stringList = entry.getTagsAsStringList();

            //Tag map processing
            for (String tagName : stringList){
                if (tagHashMap.containsKey(tagName)){
                    Tag tag = tagHashMap.get(tagName);
                    tag.addEntry(entry);
                }
                else {
                    Tag tag = new LocalTag(tagName);
                    tag.addEntry(entry);
                    tagHashMap.put(tagName,tag);
                }
            }

            //Date map processing

            Date date = entry.getDate();
            if (dateMap.containsKey(date)){
                EntryCollection collection = dateMap.get(date);
                collection.addEntry(entry);
            }
            else {
                EntryCollection collection = new LocalEntryCollection();
                collection.addEntry(entry);
                dateMap.put(date, collection);
            }
        }
        return this.entries;
    }
}
