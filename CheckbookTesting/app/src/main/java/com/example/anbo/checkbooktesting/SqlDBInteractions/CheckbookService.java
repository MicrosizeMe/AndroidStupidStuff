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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CheckbookService extends Service {

    public SQLiteDatabase db;
    private boolean tagListUpToDate = false;
    private List<String> tagList = new ArrayList<>();

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
            for (int i = 0; i < count; i++) {
                tagList.add(tagQuery.getString(0));
            }
            tagListUpToDate = true;
        }
    }

    public UUID createEntry(Calendar date, double cost, List<String> tags, String note){
        //Get date ID
        UUID dateUUID = getDateByCalendar(date);
        if (dateUUID == null) dateUUID = createDate(date);

        //Add the entry
        UUID entryUUID = UUID.randomUUID();
        String entryUUIDString = entryUUID.toString();

        ContentValues values = new ContentValues();
        values.put(CheckbookContract.ENTRY.UUID, entryUUIDString);
        values.put(CheckbookContract.ENTRY.DATE_UUID_COLUMN_NAME, dateUUID.toString());
        values.put(CheckbookContract.ENTRY.COST_COLUMN_NAME, Math.floor(cost * 100) / 100);
        values.put(CheckbookContract.ENTRY.NOTE_COLUMN_NAME, note.trim());
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
        return entryUUID;
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

    public UUID createDate(Calendar date) {
        UUID dateUUID = getDateByCalendar(date);
        if (dateUUID != null) return dateUUID;
        dateUUID = UUID.randomUUID();
        ContentValues values = new ContentValues(2);
        values.put(CheckbookContract.DATE.UUID, dateUUID.toString());
        values.put(CheckbookContract.DATE.DATE_COLUMN_NAME,
                StaticUtil.getMinutesSinceEpoch(date));
        db.insert(
                CheckbookContract.DATE.TABLE_NAME,
                null,
                values
        );
        showToast();
        return dateUUID;
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
                "WHERE " + CheckbookContract.TAG_RULES.TAG + " = " + tagUUID.toString(),
                null, null, null, null, null
        );

        int count = implicitRules.getCount();
        for (int i = 0; i < count; i++) {
            UUID implicitTag = UUID.fromString(implicitRules.getString(1));
            createEntryTagRelationship(entryUUID, implicitTag);
            implicitRules.moveToNext();
        }
        implicitRules.close();
    }

    public void createImplicitTagRelationship(String tag, String tagImplies) {
        //TODO make sure that we don't add duplicates
        UUID tagID = getTagByName(tag);
        if (tagID == null)
            tagID = createTag(tag);
        UUID tagImpliesID = getTagByName(tagImplies);
        if (tagImpliesID == null)
            tagImpliesID = createTag(tagImplies);
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
                "WHERE " + CheckbookContract.TAG.NAME_COLUMN_NAME + " = "
                        + name,
                null, null, null, null, null
        );
        if (tagQuery.getCount() == 0){
            tagQuery.close();
            return null;
        }
        UUID returnUUID = UUID.fromString(tagQuery.getString(0));
        tagQuery.close();
        return returnUUID;
    }

    private UUID getDateByCalendar (Calendar date) {
        Cursor dateQuery = db.query(
                true,
                CheckbookContract.DATE.TABLE_NAME,
                new String[]{CheckbookContract.DATE.UUID, CheckbookContract.DATE.DATE_COLUMN_NAME},
                "WHERE " + CheckbookContract.DATE.DATE_COLUMN_NAME
                        + " = " + StaticUtil.getMinutesSinceEpoch(date),
                null, null, null, null, null);
        if (dateQuery.getCount() == 0){
            dateQuery.close();
            return null;
        }
        UUID dateUUID = UUID.fromString(dateQuery.getString(0));
        dateQuery.close();
        return dateUUID;
    }
}
