package com.example.anbo.checkbooktesting.SqlDBInteractions;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.example.anbo.checkbooktesting.StaticUtil;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CheckbookService extends Service {

    SQLiteDatabase db;

    private CheckbookBinder binder = new CheckbookBinder();
    public class CheckbookBinder extends Binder{
        public CheckbookService getService(){
            return CheckbookService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        //if (db == null)
            //db = (new CheckbookSqlHelper(this)).getWritableDatabase();
        return binder;
    }

    public void showToast(){
        Toast toast = Toast.makeText(getApplicationContext(), UUID.randomUUID().toString()
                , Toast.LENGTH_SHORT);
        toast.show();
    }

    public UUID createEntry(Calendar date, double cost, List<String> tags, String note){
        //Get date ID
        Cursor dateQuery = db.query(
                true,
                CheckbookContract.DATE.TABLE_NAME,
                new String[]{CheckbookContract.DATE.UUID, CheckbookContract.DATE.DATE_COLUMN_NAME},
                "WHERE " + CheckbookContract.DATE.DATE_COLUMN_NAME
                        + " = " + StaticUtil.getStringFromCalendar(date),
                null, null, null, null, null);
        UUID dateUUID;
        if (dateQuery.getCount() == 0){
            dateUUID = createDate(date);
        }
        else dateUUID =
                UUID.fromString(dateQuery.getString(0));
        dateQuery.close();

        //Add the entry
        UUID entryUUID = UUID.randomUUID();
        String entryUUIDString = entryUUID.toString();

        ContentValues values = new ContentValues();
        values.put(CheckbookContract.ENTRY.UUID, entryUUIDString);
        values.put(CheckbookContract.ENTRY.DATE_UUID_COLUMN_NAME, dateUUID.toString());
        values.put(CheckbookContract.ENTRY.COST_COLUMN_NAME, cost);
        values.put(CheckbookContract.ENTRY.NOTE_COLUMN_NAME, note);
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
        UUID tagUUID = UUID.randomUUID();
        ContentValues values = new ContentValues(2);
        values.put(CheckbookContract.TAG.UUID, tagUUID.toString());
        values.put(CheckbookContract.TAG.NAME_COLUMN_NAME, name);
        db.insert(
                CheckbookContract.TAG.TABLE_NAME,
                null,
                values
        );
        return tagUUID;
    }

    public UUID createDate(Calendar date) {
        UUID dateUUID = UUID.randomUUID();
        ContentValues values = new ContentValues(2);
        values.put(CheckbookContract.DATE.UUID, dateUUID.toString());
        values.put(CheckbookContract.DATE.DATE_COLUMN_NAME,
                StaticUtil.getStringFromCalendar(date));
        db.insert(
                CheckbookContract.DATE.TABLE_NAME,
                null,
                values
        );
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
}
