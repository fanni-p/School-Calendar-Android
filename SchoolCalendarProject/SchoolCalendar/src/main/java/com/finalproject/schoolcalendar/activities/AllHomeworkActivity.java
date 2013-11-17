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
public class AllHomeworkActivity extends ListActivity
        implements ListView.OnItemClickListener {

    private static final int MARK_AS_DONE = 0;
    private static final int EDIT_HOMEWORK = 1;
    private static final int DELETE_HOMEWORK = 2;
    private static final String SELECTED_HOMEWORK = "SelectedHomework";

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
        getMenuInflater().inflate(R.menu.allhomework_menu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId() == android.R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(this.mAllHomeworks[info.position].getSubject());
            String[] menuItems = getResources().getStringArray(R.array.homeworks_context_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_homework:
                this.handleAddHomeworkCommand();
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
            case MARK_AS_DONE:
                this.handleMarkAsDoneCommand(this.mAllHomeworks[info.position].getId());
                return true;
            case EDIT_HOMEWORK:
                this.handleEditHomeworkCommand(this.mAllHomeworks[info.position]);
                return true;
            case DELETE_HOMEWORK:
                this.handleDeleteHomeworkCommand(this.mAllHomeworks[info.position].getId());
                return true;
            default:
                return super.onContextItemSelected(item);
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
                    AllHomeworkActivity.this.loadList();
                }
            });
        }
    }

    private void loadList() {
        this.mHomeworkArrayAdapter = new HomeworkArrayAdapter(AllHomeworkActivity.this,
                R.layout.homeworklist_item_row, AllHomeworkActivity.this.mAllHomeworks);
        this.mHomeworkList = (ListView) findViewById(android.R.id.list);
        this.mHomeworkList.setAdapter(AllHomeworkActivity.this.mHomeworkArrayAdapter);
        this.registerForContextMenu(this.mHomeworkList);
    }

    private void handleAddHomeworkCommand() {
        Intent intent = new Intent(this, AddHomeworkActivity.class);
        this.startActivity(intent);
    }

    private void handleMarkAsDoneCommand(int id) {
        final String accessToken = this.mAccessToken;
        final int subjectId = id;
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponseHelper response = DataPersister.MarkHomeworkAsDone(accessToken, subjectId);
                if (response.isStatusOk()) {
                    AllHomeworkActivity.this.getData();
                }
            }
        });
    }

    private void handleDeleteHomeworkCommand(int id) {
        final String accessToken = this.mAccessToken;
        final int subjectId = id;
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponseHelper response = DataPersister.DeleteHomework(accessToken, subjectId);
                if (response.isStatusOk()) {
                    AllHomeworkActivity.this.getData();
                }
            }
        });
    }

    private void handleEditHomeworkCommand(HomeworkModel subjectModel) {
        String subjectModelToString = this.mGson.toJson(subjectModel);

        Intent intent = new Intent(this, EditHomeworkActivity.class);
        intent.putExtra(SELECTED_HOMEWORK, subjectModelToString);
        this.startActivity(intent);
    }
}