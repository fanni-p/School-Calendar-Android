package com.finalproject.schoolcalendar.activities;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.adapters.ExpandableListAdapter;
import com.finalproject.schoolcalendar.data.DataPersister;
import com.finalproject.schoolcalendar.data.HttpResponseHelper;
import com.finalproject.schoolcalendar.helpers.NavigationDrawerManager;
import com.finalproject.schoolcalendar.helpers.SessionManager;
import com.finalproject.schoolcalendar.models.LessonModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fani on 11/16/13.
 */
public class WeekScheduleActivity extends Activity
        implements ListView.OnItemClickListener{

    private static final String MONDAY = "Monday";
    private static final String TUESDAY = "Tuesday";
    private static final String WEDNESDAY = "Wednesday";
    private static final String THURSDAY = "Thursday";
    private static final String FRIDAY = "Friday";

    private Gson mGson;
    private Handler mHandler;
    private String mAccessToken;
    private HandlerThread mHandledThread;
    private List<String> mListDataHeader;
    private ExpandableListAdapter mListAdapter;
    private ExpandableListView mExpandableListView;
    private HashMap<String, List<LessonModel>> mListDataChild;
    private NavigationDrawerManager mNavigationDrawerManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekschedule);

        this.mGson = new GsonBuilder().setDateFormat("HH:mm:ss").create();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        this.mAccessToken = user.get(SessionManager.KEY_ACCESSTOKEN);

        this.mNavigationDrawerManager = new NavigationDrawerManager();
        this.mNavigationDrawerManager.init(this, this);

        this.mHandledThread = new HandlerThread("WeekScheduleServiceThread");
        this.mHandledThread.start();

        Looper looper = this.mHandledThread.getLooper();
        if (looper != null) {
            this.mHandler = new Handler(looper);
        }

        this.mExpandableListView = (ExpandableListView) findViewById(R.id.expandable_listview);
        this.prepareListData();
    }

    private void prepareListData() {
        this.mListDataHeader = new ArrayList<String>();
        this.mListDataChild = new HashMap<String, List<LessonModel>>();
        final String accessToken = this.mAccessToken;
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponseHelper responseMonday = DataPersister.GetLessonsPerDay(MONDAY, accessToken);
                HttpResponseHelper responseTuesday = DataPersister.GetLessonsPerDay(TUESDAY, accessToken);
                HttpResponseHelper responseWednesday = DataPersister.GetLessonsPerDay(WEDNESDAY, accessToken);
                HttpResponseHelper responseThursday = DataPersister.GetLessonsPerDay(THURSDAY, accessToken);
                HttpResponseHelper responseFriday = DataPersister.GetLessonsPerDay(FRIDAY, accessToken);
                handleResponse(MONDAY, responseMonday);
                handleResponse(TUESDAY, responseTuesday);
                handleResponse(WEDNESDAY, responseWednesday);
                handleResponse(THURSDAY, responseThursday);
                handleResponse(FRIDAY, responseFriday);
                expandableListSetAdapter();
            }
        });
    }

    private void handleResponse(String currentDay, HttpResponseHelper response) {
        if (response.isStatusOk()) {
            LessonModel[] lessons = this.mGson.fromJson(response.getMessage(), LessonModel[].class);
            this.mListDataHeader.add(currentDay);
            this.mListDataChild.put(currentDay, Arrays.asList(lessons));
        }
    }

    private void expandableListSetAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WeekScheduleActivity.this.mListAdapter =
                        new ExpandableListAdapter(WeekScheduleActivity.this, WeekScheduleActivity.this.mListDataHeader, WeekScheduleActivity.this.mListDataChild);
                WeekScheduleActivity.this.mExpandableListView.setAdapter(WeekScheduleActivity.this.mListAdapter);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mHandledThread.quit();
        this.mHandledThread = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.mNavigationDrawerManager.handleSelect(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.home, menu);
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