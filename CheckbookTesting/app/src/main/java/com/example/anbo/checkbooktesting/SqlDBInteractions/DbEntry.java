package com.example.anbo.checkbooktesting.sqlDBInteractions;

import android.database.sqlite.SQLiteDatabase;

import com.example.anbo.checkbooktesting.checkbookInterface.Entry;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Anbo on 10/10/2015.
 */
public class DbEntry implements Entry {

    private SQLiteDatabase db;
    private UUID uuid;
    private Calendar date;
    private double cost;
    private List<String> tags;
    private List<UUID> tagUUIDs;
    private String note;

    public DbEntry(SQLiteDatabase db, UUID uuid, Calendar date, double cost,
                   List<String> tags, String note){
        this(db, uuid, date, cost, tags);
        this.note = note;
    }

    public DbEntry(SQLiteDatabase db,
                   UUID uuid, Calendar date, double cost, List<String> tags){
        this.db = db;
        this.uuid = uuid;
        this.date = date;
        this.cost = cost;
        this.tags = tags;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Calendar getDate() {
        return date;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public String getNote() {
        return note;
    }
}
