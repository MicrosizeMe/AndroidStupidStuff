package com.example.anbo.checkbooktesting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anbo.checkbooktesting.activityPrototypes.BaseCheckbookActivity;
import com.example.anbo.checkbooktesting.activityPrototypes.StaticCheckbookActivity;
import com.example.anbo.checkbooktesting.checkbookInterface.Entry;
import com.example.anbo.checkbooktesting.sqlDBInteractions.CheckbookService;
import com.example.anbo.checkbooktesting.sqlDBInteractions.CheckbookServiceManager;
import com.example.anbo.checkbooktesting.subcomponents.EntryListAdapter;

import java.util.List;

public class EntryCollectionViewActivity extends BaseCheckbookActivity {

    private List<Entry> previousEntry; //TODO find out if we need it
    private EntryListAdapter adapter;
    private CheckbookService checkbookService;
    private boolean isBound;

    private class EntryCollectionViewConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CheckbookService.CheckbookBinder binder = (CheckbookService.CheckbookBinder) service;
            checkbookService = binder.getService();
            isBound = true;

            List<Entry> entries = checkbookService.getPreviousSearch();
            if (entries != null) {
                previousEntry = entries;
                double totalCost = StaticUtil.sumEntries(previousEntry);
                TextView doubleText =
                        (TextView) findViewById(R.id.entry_collection_lookup_cost_text_view);
                doubleText.setText(StaticUtil.parseDouble(totalCost ));
                adapter = new EntryListAdapter(EntryCollectionViewActivity.this, previousEntry);
                ListView list =
                        (ListView) findViewById(R.id.entry_collection_lookup_entry_list_view);
                list.setAdapter(adapter);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    }

    private EntryCollectionViewConnection mConnection = new EntryCollectionViewConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_collection_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isBound){
            Intent intent = new Intent(this, CheckbookService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entry_collection_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
