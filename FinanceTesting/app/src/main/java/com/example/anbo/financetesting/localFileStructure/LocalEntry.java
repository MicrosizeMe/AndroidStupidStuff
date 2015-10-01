package com.example.anbo.financetesting.localFileStructure;

import com.example.anbo.financetesting.dbInterface.Checkbook;
import com.example.anbo.financetesting.dbInterface.Entry;
import com.example.anbo.financetesting.dbInterface.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Anbo on 9/2/2015.
 */
public class LocalEntry extends Entry {

    private double cost;
    private Date date;
    private List<Tag> tags;
    private int id;
    private LocalCheckbook checkbook;

    public LocalEntry(int id, double cost, Date date, LocalCheckbook checkbook, Tag ... tags){
        this.id = id;
        this.cost = cost;
        this.date = date;
        this.checkbook = checkbook;
        this.tags = new ArrayList<>(Arrays.asList(tags));
        Collections.sort(this.tags);
    }

    public LocalEntry(int id, double cost, LocalCheckbook checkbook, Tag ... tags){
        this(id, cost, null, checkbook, tags);
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        this.date = cal.getTime();
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public List<Tag> getTags() {
        return tags;
    }

    public int getID(){
        return id;
    }

    @Override
    public List<String> getTagsAsStringList() {
        List<String> strings = new ArrayList<>(tags.size());
        for (Tag t : tags) strings.add(t.getTagName());
        return strings;
    }

    @Override
    //Binary search to determine if it has the tag
    public boolean hasTag(String tag) {
        int lower = 0;
        int upper = tags.size();
        while (lower != upper){
            int current = (lower + upper) / 2;
            String currentTag = tags.get(current).getTagName();
            if (currentTag.compareTo(tag) == 0) return true;
            else if (currentTag.compareTo(tag) > 0) {
                upper = current;
            }
            else if (currentTag.compareTo(tag) < 0) {
                lower = current;
            }
        }
        return false;
    }

    @Override
    public boolean addTag(Tag tag) {
        tags.add(tag);
        return checkbook.updateEntry(this);
    }

    @Override
    public boolean deleteTag(Tag tag) {
        boolean foundTag = false;
        for (int i = 0; i < tags.size(); i++) {
            if (tags.equals(tag)){
                tags.remove(i);
                foundTag = true;
                break;
            }
        }
        if (foundTag) return checkbook.updateEntry(this);
        return false;
    }

    @Override
    public boolean editCost(double cost) {
        this.cost = cost;
        return checkbook.updateEntry(this);
    }

    @Override
    public boolean editDate(Date date) {
        this.date = date;
        return checkbook.updateEntry(this);
    }

    @Override
    public int compareTo(Entry e) {
        if (e.getClass() != LocalEntry.class) return super.compareTo(e);
        return this.id - ((LocalEntry) e).id;
    }
}
