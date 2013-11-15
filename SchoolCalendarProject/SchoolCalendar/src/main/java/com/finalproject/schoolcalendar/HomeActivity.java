package com.finalproject.schoolcalendar;

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
import android.widget.Toast;

import com.finalproject.schoolcalendar.data.DataPersister;
import com.finalproject.schoolcalendar.data.HttpResponseHelper;
import com.finalproject.schoolcalendar.helpers.NavigationDrawerManager;
import com.finalproject.schoolcalendar.helpers.SessionManager;
import com.finalproject.schoolcalendar.models.LessonModel;
import com.finalproject.schoolcalendar.adapters.LessonsArrayAdapter;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by Fani on 11/13/13.
 */
public class HomeActivity extends FragmentActivity
        implements ListView.OnItemClickListener {

    private Gson mGson;
    private String mUsername;
    private String mAccessToken;
    private Handler mHandler;
    private ListView mLessonsList;
    private HandlerThread mHandledThread;
    private SessionManager mSessionManager;
    private LessonsArrayAdapter mLessonArrayAdapter;
    private NavigationDrawerManager mNavigationDrawerManager;
    private LessonModel[] mLessonsPerDay;
    //private ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.mGson = new Gson();
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

        this.getData();
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

    private void getData() {
        final String accessToken = this.mAccessToken;
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponseHelper response = DataPersister.GetLessonsPerDay(accessToken);
                HomeActivity.this.handleGetLessonsResponse(response);
            }
        });
    }

    private void handleGetLessonsResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            this.mLessonsPerDay = this.mGson.fromJson(response.getMessage(), LessonModel[].class);

            HomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HomeActivity.this.mLessonArrayAdapter = new LessonsArrayAdapter(HomeActivity.this,
                            R.layout.lessonslist_item_row, HomeActivity.this.mLessonsPerDay);
                    HomeActivity.this.mLessonsList = (ListView) findViewById(R.id.lessons_listview);
                    HomeActivity.this.mLessonsList.setAdapter(HomeActivity.this.mLessonArrayAdapter);
                }
            });
        }
    }
    private void handleLogoutCommand() {
        final String accessToken = this.mAccessToken;
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponseHelper response = DataPersister.Logout(accessToken);
                HomeActivity.this.handleLogoutResponse(response);
            }
        });
    }

    private void handleLogoutResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            this.mSessionManager.logoutUser();
        } else {
            Toast.makeText(this, "Logout failed. Please try again!", Toast.LENGTH_LONG).show();
        }
    }
}

