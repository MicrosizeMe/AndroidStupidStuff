package com.example.anbo.checkbooktesting.sqlDBInteractions;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.anbo.checkbooktesting.StaticUtil;

/**
 * Created by Anbo on 10/4/2015.
 */
public class CheckbookServiceManager {

    public CheckbookService service = null;
    public boolean isBound = false;

    public class CheckbookServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            //Todo figure out how to do this intelligently
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            CheckbookService.CheckbookBinder binder = (CheckbookService.CheckbookBinder) service;
            CheckbookServiceManager.this.service = binder.getService();
            CheckbookServiceManager.this.isBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            CheckbookServiceManager.this.isBound = false;
        }
    }

    public ServiceConnection mConnection;

    public CheckbookServiceManager(){
        mConnection = new CheckbookServiceConnection();
    }

    public CheckbookServiceManager(ServiceConnection connection) {
        mConnection = connection;
    }
}
