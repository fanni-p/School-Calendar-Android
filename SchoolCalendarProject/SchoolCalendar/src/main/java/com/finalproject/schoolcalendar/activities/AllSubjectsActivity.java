package com.finalproject.schoolcalendar.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.ContextMenu;
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
public class AllSubjectsActivity extends ListActivity
        implements ListView.OnItemClickListener {

    private static final int EDIT_SUBJECT = 0;
    private static final int DELETE_SUBJECT = 1;
    private static final String SELECTED_SUBJECT = "SelectedSubject";

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
        getMenuInflater().inflate(R.menu.allsubjects_menu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == android.R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(this.mAllSubjects[info.position].getName());
            String[] menuItems = getResources().getStringArray(R.array.edit_delete_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_subject:
                this.handleAddSubjectCommand();
                return true;
            default:
                this.mNavigationDrawerManager.handleOnOptionsItemSelected(item);
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case EDIT_SUBJECT:
                this.handleEditSubjectCommand(this.mAllSubjects[info.position]);
                return true;
            case DELETE_SUBJECT:
                //this.handleAddSubjectCommand();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void handleEditSubjectCommand(SubjectModel subjectModel) {
        String subjectModelToString = this.mGson.toJson(subjectModel);

        Intent intent = new Intent(this, EditSubjectActivity.class);
        intent.putExtra(SELECTED_SUBJECT, subjectModelToString);
        this.startActivity(intent);
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
                HttpResponseHelper response = DataPersister.GetAllSubjects(accessToken);
                AllSubjectsActivity.this.handleGetAllSubjectsResponse(response);
            }
        });
    }

    private void handleGetAllSubjectsResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            this.mAllSubjects = this.mGson.fromJson(response.getMessage(), SubjectModel[].class);
            AllSubjectsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadListView();
                }
            });
        }
    }

    private void loadListView() {
        this.mSubjectArrayAdapter = new SubjectsArrayAdapter(this,
                R.layout.subjectlist_item_row, this.mAllSubjects);
        this.mSubjectsList = (ListView) findViewById(android.R.id.list);
        this.mSubjectsList.setAdapter(this.mSubjectArrayAdapter);
        this.registerForContextMenu(this.mSubjectsList);
    }

    private void handleAddSubjectCommand() {
        Intent intent = new Intent(this, AddSubjectActivity.class);
        this.startActivity(intent);
    }
}