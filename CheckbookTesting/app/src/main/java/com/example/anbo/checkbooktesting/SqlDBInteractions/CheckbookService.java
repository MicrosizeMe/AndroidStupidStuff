package com.example.anbo.checkbooktesting.sqlDBInteractions;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.example.anbo.checkbooktesting.StaticUtil;
import com.example.anbo.checkbooktesting.checkbookInterface.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CheckbookService extends Service {

    public SQLiteDatabase db;
    private boolean tagListUpToDate = false;
    private List<String> tagList = new ArrayList<>();
    private List<Entry> previousSearch;

    private CheckbookBinder binder = new CheckbookBinder();
    public class CheckbookBinder extends Binder{
        public CheckbookService getService(){
            return CheckbookService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (db == null){
            CheckbookSqlHelper helper = new CheckbookSqlHelper(this);
            db = helper.getWritableDatabase();
            helper.resetDb(db);
        }
        return binder;
    }

    public void showToast(){
        Toast toast = Toast.makeText(getApplicationContext(), UUID.randomUUID().toString()
                , Toast.LENGTH_SHORT);
        toast.show();
    }

    public String[] getTagList(){
        if (!tagListUpToDate) updateTagList();
        String[] returnArray = new String[tagList.size()];
        return tagList.toArray(returnArray);
    }

    private void updateTagList(){
        if (!tagListUpToDate){
            Cursor tagQuery = db.query(
                    false,
                    CheckbookContract.TAG.TABLE_NAME,
                    new String[]{CheckbookContract.TAG.NAME_COLUMN_NAME},
                    null, null, null, null, null, null
            );
            tagList = new ArrayList<>();
            int count = tagQuery.getCount();
            if (count != 0) tagQuery.moveToFirst();
            for (int i = 0; i < count; i++) {
                tagList.add(tagQuery.getString(0));
                tagQuery.moveToNext();
            }
            tagListUpToDate = true;
            tagQuery.close();
        }
    }

    public UUID createEntry(Calendar date, double cost, List<String> tags, String note){
        //Add the entry
        UUID entryUUID = UUID.randomUUID();
        String entryUUIDString = entryUUID.toString();

        ContentValues values = new ContentValues();
        values.put(CheckbookContract.ENTRY.UUID, entryUUIDString);
        values.put(CheckbookContract.ENTRY.DATE_COLUMN_NAME, StaticUtil.getMinutesSinceEpoch(date));
        values.put(CheckbookContract.ENTRY.COST_COLUMN_NAME, Math.floor(cost * 100) / 100);
        values.put(CheckbookContract.ENTRY.NOTE_COLUMN_NAME, note == null ? null : note.trim());
        db.insert(
                CheckbookContract.ENTRY.TABLE_NAME,
                null,
                values
        );

        //getTags
        for (String tagName : tags){
            UUID tagUUID = getTagByName(tagName);
            if (tagUUID == null) {
                tagUUID = createTag(tagName);
            }
            createEntryTagRelationship(entryUUID, tagUUID);
        }
        this.showToast();
        return entryUUID;
    }

    public UUID editEntry(String uuidString, Calendar date,
                          double cost, List<String> tags, String note) {
        //TODO finish edit logic
        /*Cursor entryQuery = db.query(
                false,
                CheckbookContract.ENTRY.TABLE_NAME,
                new String[] {
                        CheckbookContract.ENTRY.DATE_COLUMN_NAME,
                        CheckbookContract.ENTRY.COST_COLUMN_NAME,
                        CheckbookContract.ENTRY.NOTE_COLUMN_NAME
                },
        );*/
        return null;
    }

    public UUID createTag(String name) {
        UUID tagUUID = getTagByName(name);
        if (tagUUID != null) return tagUUID;
        tagUUID = UUID.randomUUID();
        ContentValues values = new ContentValues(2);
        values.put(CheckbookContract.TAG.UUID, tagUUID.toString());
        values.put(CheckbookContract.TAG.NAME_COLUMN_NAME, name);
        db.insert(
                CheckbookContract.TAG.TABLE_NAME,
                null,
                values
        );
        tagListUpToDate = false;
        return tagUUID;
    }

    private void createEntryTagRelationship(UUID entryUUID, UUID tagUUID) {
        ContentValues tagValues = new ContentValues();
        tagValues.put(CheckbookContract.ENTRY_TO_TAG.ENTRY_UUID_COLUMN_NAME, entryUUID.toString());
        tagValues.put(CheckbookContract.ENTRY_TO_TAG.TAG_UUID_COLUMN_NAME, tagUUID.toString());
        db.insert(
                CheckbookContract.ENTRY_TO_TAG.TABLE_NAME,
                null,
                tagValues
        );

        Cursor implicitRules = db.query(
                false,
                CheckbookContract.TAG_RULES.TABLE_NAME,
                new String[] {
                        CheckbookContract.TAG_RULES.TAG,
                        CheckbookContract.TAG_RULES.TAG_IMPLIES
                },
                CheckbookContract.TAG_RULES.TAG + " = '" + tagUUID.toString() + "'",
                null, null, null, null, null
        );

        int count = implicitRules.getCount();
        if (count != 0) implicitRules.moveToFirst();
        for (int i = 0; i < count; i++) {
            UUID implicitTag = UUID.fromString(implicitRules.getString(1));
            createEntryTagRelationship(entryUUID, implicitTag);
            implicitRules.moveToNext();
        }
        implicitRules.close();
    }

    public void createImplicitTagRelationship(String tag, String tagImplies) {
        UUID tagID = getTagByName(tag);
        if (tagID == null)
            tagID = createTag(tag);
        UUID tagImpliesID = getTagByName(tagImplies);
        if (tagImpliesID == null)
            tagImpliesID = createTag(tagImplies);
        Cursor tagQuery = db.query(
                false,
                CheckbookContract.TAG_RULES.TABLE_NAME,
                new String[] {CheckbookContract.TAG_RULES.TAG,
                        CheckbookContract.TAG_RULES.TAG_IMPLIES},
                CheckbookContract.TAG_RULES.TAG + " = '" + tag + "'" +
                        " AND "
                        + CheckbookContract.TAG_RULES.TAG_IMPLIES + " = '" + tagImplies + "'",
                null, null, null, null, null
        );
        int count = tagQuery.getCount();
        tagQuery.close();
        if (count != 0) return;

        ContentValues values = new ContentValues(2);
        values.put(CheckbookContract.TAG_RULES.TAG, tagID.toString());
        values.put(CheckbookContract.TAG_RULES.TAG_IMPLIES, tagImpliesID.toString());
        db.insert(CheckbookContract.TAG_RULES.TABLE_NAME, null, values);
    }

    private UUID getTagByName(String name) {
        Cursor tagQuery = db.query(
                false,
                CheckbookContract.TAG.TABLE_NAME,
                new String[]{CheckbookContract.TAG.UUID, CheckbookContract.TAG.NAME_COLUMN_NAME},
                CheckbookContract.TAG.NAME_COLUMN_NAME + " = '" + name + "'",
                null, null, null, null, null
        );
        if (tagQuery.getCount() == 0){
            tagQuery.close();
            return null;
        }
        tagQuery.moveToFirst();
        UUID returnUUID = UUID.fromString(tagQuery.getString(0));
        tagQuery.close();
        return returnUUID;
    }

    public List<Entry> findEntries(double costLower, double costUpper,
                                   Calendar dateLower, Calendar dateUpper,
                                   List<String> tagList){
        List<String> whereClauses = new ArrayList<>();
        if (costLower >= 0) {
            whereClauses.add(CheckbookContract.ENTRY.TABLE_NAME + "."
                    + CheckbookContract.ENTRY.COST_COLUMN_NAME + " >= "
                    + Math.floor(costLower * 100) / 100);
        }
        if (costUpper >= 0) {
            whereClauses.add(CheckbookContract.ENTRY.TABLE_NAME + "."
                    + CheckbookContract.ENTRY.COST_COLUMN_NAME + " <= "
                    + Math.floor(costLower * 100) / 100);
        }
        if (dateLower != null) {
            whereClauses.add(CheckbookContract.ENTRY.TABLE_NAME + "."
                    + CheckbookContract.ENTRY.DATE_COLUMN_NAME + " >= "
                    + StaticUtil.getMinutesSinceEpoch(dateLower));
        }
        if (dateUpper != null) {
            whereClauses.add(CheckbookContract.ENTRY.TABLE_NAME + "."
                    + CheckbookContract.ENTRY.DATE_COLUMN_NAME + " >= "
                    + StaticUtil.getMinutesSinceEpoch(dateLower));
        }

        List<String> tagClauses = new ArrayList<>();
        for (String tag : tagList) {
            tagClauses.add(
                    CheckbookContract.TAG.TABLE_NAME + "."
                            + CheckbookContract.TAG.NAME_COLUMN_NAME + " = '" + tag + "'"
            );
        }

        String whereStatement = "";
        int count = whereClauses.size();
        if (count != 0){
            whereStatement += "WHERE (" + whereClauses.get(0) + ") ";
            for (int i = 1; i < count; i++) {
                whereStatement += " AND (" + whereClauses.get(i) + ") ";
            }
        }

        count = tagClauses.size();
        if (count != 0) {
            if (whereStatement.isEmpty()) whereStatement += "WHERE (";
            else whereStatement += " AND (";
            whereStatement += "(" + tagClauses.get(0) + ")";
            for (int i = 1; i < count; i++) {
                whereStatement += " OR (" + tagClauses.get(i) + ") ";
            }
            whereStatement +=  ") ";
        }

        String rawQuery = "SELECT "
                + CheckbookContract.ENTRY.TABLE_NAME + "."
                    + CheckbookContract.ENTRY.UUID + " , "
                + CheckbookContract.ENTRY.TABLE_NAME + "."
                    + CheckbookContract.ENTRY.DATE_COLUMN_NAME + " , "
                + CheckbookContract.ENTRY.TABLE_NAME + "."
                    + CheckbookContract.ENTRY.COST_COLUMN_NAME + " , "
                + CheckbookContract.ENTRY.TABLE_NAME + "."
                    + CheckbookContract.ENTRY.NOTE_COLUMN_NAME + " , "
                + CheckbookContract.TAG.TABLE_NAME + "."
                    + CheckbookContract.TAG.NAME_COLUMN_NAME;
        rawQuery += " FROM " + CheckbookContract.ENTRY.TABLE_NAME;
        rawQuery += " JOIN " + CheckbookContract.ENTRY_TO_TAG.TABLE_NAME
                + " ON ("
                    + CheckbookContract.ENTRY.TABLE_NAME + "." + CheckbookContract.ENTRY.UUID
                    + " = "
                    + CheckbookContract.ENTRY_TO_TAG.TABLE_NAME
                        + "." + CheckbookContract.ENTRY_TO_TAG.ENTRY_UUID_COLUMN_NAME
                + ") ";
        rawQuery += " JOIN " + CheckbookContract.TAG.TABLE_NAME
                + " ON ("
                    + CheckbookContract.ENTRY_TO_TAG.TABLE_NAME
                        + "." + CheckbookContract.ENTRY_TO_TAG.TAG_UUID_COLUMN_NAME
                    + " = "
                    + CheckbookContract.TAG.TABLE_NAME
                        + "." + CheckbookContract.TAG.UUID
                + ") ";
        rawQuery += whereStatement;

        Cursor queryResults = db.rawQuery(rawQuery, null);

        queryResults.moveToFirst();
        List<Entry> entries = new ArrayList<>();
        while (!queryResults.isAfterLast()) {
            String uuidString = queryResults.getString(0);
            UUID uuid = UUID.fromString(uuidString);
            Calendar cal = StaticUtil.getCalendarFromMinutes(queryResults.getLong(1));
            double cost = queryResults.getDouble(2);
            String note = queryResults.getString(3);
            List<String> tags = new ArrayList<>();
            for (
                    ;
                    !queryResults.isAfterLast() && queryResults.getString(0).equals(uuidString);
                    queryResults.moveToNext()) {
                    tags.add(queryResults.getString(4));
            }
            Entry currentEntry = new DbEntry(db, uuid, cal, cost, tags, note);
            entries.add(currentEntry);
        }
        queryResults.close();
        previousSearch = entries;
        return entries;
    }

    public double sumEntries(List<Entry> entries) {
        double sum = 0.0;
        for (Entry entry : entries) {
            sum += entry.getCost();
        }
        return sum;
    }

    public List<Entry> getPreviousSearch(){
        return previousSearch;
    }
}
