package com.example.anbo.financetesting.dbInterface;

/**
 * Created by Anbo on 9/2/2015.
 */
public interface Tag extends EntryCollection, Comparable<Tag>{

    //Implied to be case insensitive
    public abstract String getTagName();
}
