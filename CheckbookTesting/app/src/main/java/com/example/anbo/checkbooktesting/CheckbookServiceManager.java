package com.example.anbo.checkbooktesting;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.anbo.checkbooktesting.sqlDBInteractions.CheckbookService;

/**
 * Created by Anbo on 10/4/2015.
 */
public class CheckbookServiceManager {

    public CheckbookService service = null;
    public boolean isBound = false;

    public ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            CheckbookService.CheckbookBinder binder = (CheckbookService.CheckbookBinder) service;
            CheckbookServiceManager.this.service = binder.getService();
            CheckbookServiceManager.this.isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            CheckbookServiceManager.this.isBound = false;
        }
    };

    public CheckbookServiceManager(){

    }
}
