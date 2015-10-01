package com.example.anbo.financetesting.localFileStructure;

import com.example.anbo.financetesting.dbInterface.Entry;
import com.example.anbo.financetesting.dbInterface.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anbo on 9/3/2015.
 */
public class LocalTag extends LocalEntryCollection implements Tag{

    private String tagName;

    public LocalTag(String tagName, List<Entry> entries) {
        super(entries);
        this.tagName = tagName;
    }

    public LocalTag(String tagName) {
        super();
        this.tagName = tagName;
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    public int compareTo(Tag o) {
        return getTagName().compareTo(o.getTagName());
    }
}
