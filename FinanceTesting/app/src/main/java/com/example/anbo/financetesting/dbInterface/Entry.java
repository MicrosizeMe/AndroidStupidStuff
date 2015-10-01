package com.example.anbo.financetesting.dbInterface;

import java.util.Date;
import java.util.List;

/**
 * Created by Anbo on 9/2/2015.
 */
public abstract class Entry implements Comparable<Entry>{
    public abstract double getCost();

    public abstract Date getDate();

    public abstract List<Tag> getTags();

    public abstract List<String> getTagsAsStringList();

    public abstract boolean hasTag(String tag);

    public boolean hasTag(Tag tag){
        return hasTag(tag.getTagName());
    }

    public abstract boolean addTag(Tag tag);

    public abstract boolean deleteTag(Tag tag);

    public abstract boolean editCost(double cost);

    public abstract boolean editDate(Date date);

    @Override
    public int compareTo(Entry o){
        int dateCompare = getDate().compareTo(o.getDate());
        if (dateCompare != 0) return dateCompare;
        double costDiff = getCost() - o.getCost();
        if (costDiff > 0) return 1;
        else if (costDiff < 0) return -1;

        List<String> myTags = getTagsAsStringList();
        List<String> theirTags = o.getTagsAsStringList();

        for (int i = 0; i < myTags.size(); i++) {
            if (!theirTags.contains(myTags.get(i))) return 1;
        }

        for (int i = 0; i < theirTags.size(); i++) {
            if (!myTags.contains(theirTags.get(i))) return -1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o){
        if (o.getClass() != Entry.class) return false;
        return compareTo((Entry)o) == 0;
    }
}
