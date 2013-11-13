package com.finalproject.schoolcalendar;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.finalproject.schoolcalendar.data.DataPersister;
import com.finalproject.schoolcalendar.data.HttpResponse;
import com.finalproject.schoolcalendar.helpers.NavigationDrawerManager;
import com.finalproject.schoolcalendar.helpers.SessionManager;

import java.util.HashMap;

/**
 * Created by Fani on 11/13/13.
 */
public class HomeActivity extends FragmentActivity
        implements ListView.OnItemClickListener {

    private String mUsername;
    private String mAccessToken;
    private Handler mHandler;
    private HandlerThread mHandledThread;
    private SessionManager mSessionManager;
    private NavigationDrawerManager mNavigationDrawerManager;
    //private ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.mSessionManager = new SessionManager(getApplicationContext());

        this.mSessionManager.checkLogin();
        HashMap<String, String> user = this.mSessionManager.getUserDetails();
        this.mUsername = user.get(SessionManager.KEY_NAME);
        this.mAccessToken = user.get(SessionManager.KEY_ACCESSTOKEN);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        //mCoursePagerAdapter = new CoursePagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById(R.id.pager);
        //mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setAdapter(mCoursePagerAdapter);

        this.mNavigationDrawerManager = new NavigationDrawerManager();
        this.mNavigationDrawerManager.init(this, this);

        this.mHandledThread = new HandlerThread("UserServiceThread");
        this.mHandledThread.start();

        Looper looper = this.mHandledThread.getLooper();
        if (looper != null) {
            this.mHandler = new Handler(looper);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mHandledThread.quit();
        this.mHandledThread = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //mCoursePagerAdapter.setCourseLib(optionLib);
        this.mNavigationDrawerManager.handleSelect(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                this.handleLogoutCommand();
                return true;
            default:
                this.mNavigationDrawerManager.handleOnOptionsItemSelected(item);
                return super.onOptionsItemSelected(item);
        }
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

    private void handleLogoutCommand() {
        final String accessToken = this.mAccessToken;
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponse response = DataPersister.Logout(accessToken);
                handleLogoutResponse(response);
            }
        });
    }

    private void handleLogoutResponse(HttpResponse response) {
        if (response.isStatusOk()) {
            this.mSessionManager.logoutUser();
        } else {
            //TODO  Create Toast Notification
        }
    }
}