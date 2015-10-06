package com.example.anbo.checkbooktesting;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.example.anbo.checkbooktesting.CheckbookInterface.Checkbook;

public class CheckbookService extends Service implements Checkbook {

    private CheckbookBinder binder = new CheckbookBinder();
    public class CheckbookBinder extends Binder{
        public CheckbookService getService(){
            return CheckbookService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void showToast(){
        Toast toast = Toast.makeText(getApplicationContext(), "Hello!", Toast.LENGTH_SHORT);
        toast.show();
    }
}
