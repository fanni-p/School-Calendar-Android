package com.finalproject.schoolcalendar.activities;

import android.app.ListActivity;
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
import com.finalproject.schoolcalendar.adapters.SubjectsArrayAdapter;
import com.finalproject.schoolcalendar.data.DataPersister;
import com.finalproject.schoolcalendar.data.HttpResponseHelper;
import com.finalproject.schoolcalendar.helpers.NavigationDrawerManager;
import com.finalproject.schoolcalendar.helpers.SessionManager;
import com.finalproject.schoolcalendar.models.SubjectModel;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by Fani on 11/16/13.
 */
public class AllSubjects extends ListActivity
        implements ListView.OnItemClickListener {

    private Gson mGson;
    private String mAccessToken;
    private Handler mHandler;
    private ListView mSubjectsList;
    private SubjectModel[] mAllSubjects;
    private HandlerThread mHandledThread;
    private SubjectsArrayAdapter mSubjectArrayAdapter;
    private NavigationDrawerManager mNavigationDrawerManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_subjects);

        this.mGson = new Gson();
        SessionManager sessionManager = new SessionManager(getApplicationContext());

        HashMap<String, String> user = sessionManager.getUserDetails();
        this.mAccessToken = user.get(SessionManager.KEY_ACCESSTOKEN);

        this.mNavigationDrawerManager = new NavigationDrawerManager();
        this.mNavigationDrawerManager.init(this, this);

        this.mHandledThread = new HandlerThread("SubjectServiceThread");
        this.mHandledThread.start();

        Looper looper = this.mHandledThread.getLooper();
        if (looper != null) {
            this.mHandler = new Handler(looper);
        }

        this.getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mHandledThread.quit();
        this.mHandledThread = null;
    }

    private void getData() {
        final String accessToken = this.mAccessToken;
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponseHelper response = DataPersister.GetAllSubjects(accessToken);
                AllSubjects.this.handleGetAllSubjectsResponse(response);
            }
        });
    }

    private void handleGetAllSubjectsResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            this.mAllSubjects = this.mGson.fromJson(response.getMessage(), SubjectModel[].class);

            AllSubjects.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AllSubjects.this.mSubjectArrayAdapter = new SubjectsArrayAdapter(AllSubjects.this,
                            R.layout.subjectlist_item_row, AllSubjects.this.mAllSubjects);
                    AllSubjects.this.mSubjectsList = (ListView) findViewById(android.R.id.list);
                    AllSubjects.this.mSubjectsList.setAdapter(AllSubjects.this.mSubjectArrayAdapter);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //mCoursePagerAdapter.setCourseLib(optionLib);
        this.mNavigationDrawerManager.handleSelect(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.home, menu);
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