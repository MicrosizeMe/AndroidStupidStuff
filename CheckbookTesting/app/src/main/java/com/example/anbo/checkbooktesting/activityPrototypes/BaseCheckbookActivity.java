package com.example.anbo.checkbooktesting.activityPrototypes;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.example.anbo.checkbooktesting.CostRecordingActivity;
import com.example.anbo.checkbooktesting.R;
import com.example.anbo.checkbooktesting.SearchEntryActivity;

/**
 * Created by Anbo on 10/22/2015.
 */
public abstract class BaseCheckbookActivity extends Activity{
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add_entry) {
            Intent intent = new Intent(this, CostRecordingActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchEntryActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
