package com.finalproject.schoolcalendar.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.helpers.NavigationDrawerManager;

/**
 * Created by Fani on 11/16/13.
 */
public class WeekScheduleActivity extends FragmentActivity
        implements ListView.OnItemClickListener{

    private NavigationDrawerManager mNavigationDrawerManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekschedule);

        this.mNavigationDrawerManager = new NavigationDrawerManager();
        this.mNavigationDrawerManager.init(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //mCoursePagerAdapter.setCourseLib(optionLib);
        this.mNavigationDrawerManager.handleSelect(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_logout:
//                handleLogoutCommand();
//                return true;
//            default:
                this.mNavigationDrawerManager.handleOnOptionsItemSelected(item);
                return super.onOptionsItemSelected(item);
//        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mNavigationDrawerManager.syncState();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.mNavigationDrawerManager.handleOnPrepareOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        this.mNavigationDrawerManager.syncState();
        super.onConfigurationChanged(newConfig);
    }
}