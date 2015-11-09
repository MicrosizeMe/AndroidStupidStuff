package com.example.anbo.checkbooktesting.activityPrototypes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;

import com.example.anbo.checkbooktesting.CostRecordingActivity;
import com.example.anbo.checkbooktesting.R;
import com.example.anbo.checkbooktesting.SearchEntryActivity;
import com.example.anbo.checkbooktesting.sqlDBInteractions.CheckbookService;
import com.example.anbo.checkbooktesting.sqlDBInteractions.CheckbookServiceManager;

public abstract class StaticCheckbookActivity extends BaseCheckbookActivity {

    protected CheckbookServiceManager serviceManager = new CheckbookServiceManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!serviceManager.isBound){
            Intent intent = new Intent(this, CheckbookService.class);
            bindService(intent, serviceManager.mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceManager.isBound) {
            unbindService(serviceManager.mConnection);
            serviceManager.isBound = false;
        }
    }
}
