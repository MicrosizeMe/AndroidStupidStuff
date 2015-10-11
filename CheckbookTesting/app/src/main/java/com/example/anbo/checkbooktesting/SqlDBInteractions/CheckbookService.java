package com.example.anbo.checkbooktesting.SqlDBInteractions;

import android.app.Service;
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

    public void createEntry(Calendar date, double cost, List<String> tags, String note){
        //Get date ID
        String[] columns = {CheckbookContract.DATE.UUID, CheckbookContract.DATE.DATE_COLUMN_NAME};
        Cursor dateQuery = db.query(
                true,
                CheckbookContract.DATE.TABLE_NAME,
                columns,
                "WHERE " + CheckbookContract.DATE.DATE_COLUMN_NAME
                        + " = " + StaticUtil.getStringFromCalendar(date),
                null, null, null, null, null);
        UUID dateUUID;
        if (dateQuery.getCount() == 0){
            dateUUID = UUID.randomUUID();
        }
        else dateUUID =
                UUID.fromString(dateQuery.getString(CheckbookContract.ENTRY.DATE_COLUMN_INDEX));
    }

    public void createEntry(Calendar date, double cost, List<String> tags){

    }
}
