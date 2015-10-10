package com.example.anbo.checkbooktesting.SqlDBInteractions;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

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
        if (db == null)
            db = (new CheckbookSqlHelper(this)).getWritableDatabase();
        return binder;
    }

    public void showToast(){
        Toast toast = Toast.makeText(getApplicationContext(), "Hello!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void createEntry(Calendar date, double cost, List<String> tags, String note){
        //Get date ID
        db.qu
    }

    public void createEntry(Calendar date, double cost, List<String> tags){

    }
}
