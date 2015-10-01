package com.example.anbo.financetesting.dbInterface;

import java.util.Date;

/**
 * Created by Anbo on 9/2/2015.
 */
public interface Checkbook extends EntryCollection{

    boolean remove(int i);

    //Creates a tag with the specified string name. Returns that tag, or the existing tag if it
    //already exists.
    Tag createTag(String tagName);

    Tag getTag(String tagName);

    Entry createEntry(double cost, Date date, String ... tagNames);
}
