package com.finalproject.schoolcalendar.activities;

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

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.adapters.HomeworkArrayAdapter;
import com.finalproject.schoolcalendar.data.DataPersister;
import com.finalproject.schoolcalendar.data.HttpResponseHelper;
import com.finalproject.schoolcalendar.helpers.NavigationDrawerManager;
import com.finalproject.schoolcalendar.helpers.SessionManager;
import com.finalproject.schoolcalendar.models.HomeworkModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

/**
 * Created by Fani on 11/16/13.
 */
public class AllHomeworkActivity extends FragmentActivity
        implements ListView.OnItemClickListener {

    private Gson mGson;
    private String mAccessToken;
    private Handler mHandler;
    private ListView mHomeworkList;
    private HomeworkModel[] mAllHomeworks;
    private HandlerThread mHandledThread;
    private HomeworkArrayAdapter mHomeworkArrayAdapter;
    private NavigationDrawerManager mNavigationDrawerManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_homework);

        this.mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        this.mAccessToken = user.get(SessionManager.KEY_ACCESSTOKEN);

        this.mNavigationDrawerManager = new NavigationDrawerManager();
        this.mNavigationDrawerManager.init(this, this);

        this.mHandledThread = new HandlerThread("HomeworkServiceThread");
        this.mHandledThread.start();

        Looper looper = this.mHandledThread.getLooper();
        if (looper != null) {
            this.mHandler = new Handler(looper);
        }

        this.getData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
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

    private void getData() {
        final String accessToken = this.mAccessToken;
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponseHelper response = DataPersister.GetAllHomework(accessToken);
                AllHomeworkActivity.this.handleGetAllHomeworkResponse(response);
            }
        });
    }

    private void handleGetAllHomeworkResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            this.mAllHomeworks = this.mGson.fromJson(response.getMessage(), HomeworkModel[].class);

            AllHomeworkActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AllHomeworkActivity.this.mHomeworkArrayAdapter = new HomeworkArrayAdapter(AllHomeworkActivity.this,
                            R.layout.homeworklist_item_row, AllHomeworkActivity.this.mAllHomeworks);
                    AllHomeworkActivity.this.mHomeworkList = (ListView) findViewById(android.R.id.list);
                    AllHomeworkActivity.this.mHomeworkList.setAdapter(AllHomeworkActivity.this.mHomeworkArrayAdapter);
                }
            });
        }
    }
}