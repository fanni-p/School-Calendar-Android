package com.finalproject.schoolcalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.data.DataPersister;
import com.finalproject.schoolcalendar.data.HttpResponseHelper;
import com.finalproject.schoolcalendar.helpers.SessionManager;
import com.finalproject.schoolcalendar.models.HomeworkModel;
import com.finalproject.schoolcalendar.models.SubjectModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fani on 11/17/13.
 */
public class AddHomeworkActivity extends Activity {

    private static final String LAST_ACTIVITY = "LastActivity";

    private Gson mGson;
    private Handler mHandler;
    private String mAccessToken;
    private ArrayList<String> mSubjects;
    private HandlerThread mHandledThread;
    private SubjectModel[] mSubjectsModels;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);

        this.mGson = new Gson();

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        this.mAccessToken = user.get(SessionManager.KEY_ACCESSTOKEN);

        this.mHandledThread = new HandlerThread("AddHomeworkServiceThread");
        this.mHandledThread.start();

        Looper looper = this.mHandledThread.getLooper();
        if (looper != null) {
            this.mHandler = new Handler(looper);
        }

        this.setupButtons();
        this.getSpinnerData();
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

    private void setupButtons() {
        Button addButton = (Button) this.findViewById(R.id.add_homework_addbutton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddHomeworkActivity.this.handleAddHomeworkButtonCommand();
            }
        });

        Button addNewSubjectButton = (Button) this.findViewById(R.id.add_homework_addNewSubjectButton);
        addNewSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddHomeworkActivity.this.handleAddNewSubjectButtonCommand();
            }
        });
    }

    private void handleAddHomeworkButtonCommand() {
        final String accessToken = this.mAccessToken;
        final HomeworkModel homeworkModel = this.createHomeworkModel();

        if (homeworkModel != null) {
            this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    HttpResponseHelper response = DataPersister.AddNewHomework(homeworkModel, accessToken);
                    AddHomeworkActivity.this.handleAddHomeworkResponse(response);
                }
            });
        }
    }

    private void handleAddNewSubjectButtonCommand() {
        SharedPreferences preferences = getSharedPreferences(LAST_ACTIVITY, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LAST_ACTIVITY, this.getClass().getName());
        editor.commit();

        Intent intent = new Intent(this, AddSubjectActivity.class);
        this.startActivity(intent);
    }

    private void getSpinnerData() {
        final String accessToken = this.mAccessToken;
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponseHelper response = DataPersister.GetAllSubjects(accessToken);
                AddHomeworkActivity.this.handleGetAllSubjectsResponse(response);
            }
        });
    }

    private void setupSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.add_homework_spinner);
        spinner.setAdapter(new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, this.mSubjects));

    }

    private void handleGetAllSubjectsResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            this.mSubjectsModels = this.mGson.fromJson(response.getMessage(), SubjectModel[].class);
            this.mSubjects = SubjectModel.getNames(this.mSubjectsModels);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AddHomeworkActivity.this.setupSpinner();
                }
            });
        }
    }

    private void handleAddHomeworkResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            Intent intent = new Intent(this, AllHomeworkActivity.class);
            this.startActivity(intent);
            finish();
        }
    }

    private HomeworkModel createHomeworkModel() {
        Spinner subjectNameBox = (Spinner) this.findViewById(R.id.add_homework_spinner);
        DatePicker submitDateBox = (DatePicker) this.findViewById(R.id.add_homework_datepicker);

        int day = submitDateBox.getDayOfMonth();
        int month = submitDateBox.getMonth();
        int year = submitDateBox.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        String subjectName = this.mSubjects.get(subjectNameBox.getSelectedItemPosition());
        Date submitDate = calendar.getTime();

        HomeworkModel homeworkModel = new HomeworkModel(subjectName, submitDate);

        return homeworkModel;
    }
}