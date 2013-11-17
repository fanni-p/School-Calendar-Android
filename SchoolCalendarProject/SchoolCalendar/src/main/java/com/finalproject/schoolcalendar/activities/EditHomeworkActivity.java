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
import android.widget.TextView;

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.data.DataPersister;
import com.finalproject.schoolcalendar.data.HttpResponseHelper;
import com.finalproject.schoolcalendar.helpers.SessionManager;
import com.finalproject.schoolcalendar.models.HomeworkModel;
import com.finalproject.schoolcalendar.models.SubjectModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fani on 11/17/13.
 */
public class EditHomeworkActivity extends Activity {

    private static final String LAST_ACTIVITY = "LastActivity";
    private static final String SELECTED_HOMEWORK = "SelectedHomework";

    private Gson mGson;
    private Handler mHandler;
    private String mAccessToken;
    private ArrayList<String> mSubjects;
    private HandlerThread mHandledThread;
    private HomeworkModel mSelectedHomework;
    private DatePicker mSubmitDateBox;
    private Spinner mSubjectNameBox;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);

        this.mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Intent intent = getIntent();
        String selectedSubject = intent.getStringExtra(SELECTED_HOMEWORK);
        this.mSelectedHomework = this.mGson.fromJson(selectedSubject, HomeworkModel.class);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        this.mAccessToken = user.get(SessionManager.KEY_ACCESSTOKEN);

        this.mHandledThread = new HandlerThread("EditHomeworkServiceThread");
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
        Button editButton = (Button) this.findViewById(R.id.add_homework_addbutton);
        editButton.setText(R.string.edit_homework_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditHomeworkActivity.this.handleAddHomeworkButtonCommand();
            }
        });

        Button addNewSubjectButton = (Button) this.findViewById(R.id.add_homework_addNewSubjectButton);
        addNewSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditHomeworkActivity.this.handleAddNewSubjectButtonCommand();
            }
        });
    }

    private void setupFields() {
        TextView pageName = (TextView) this.findViewById(R.id.add_homework_pageName);
        pageName.setText(R.string.edit_homework_page_name);

        this.mSubjectNameBox = (Spinner) this.findViewById(R.id.add_homework_spinner);
        this.mSubmitDateBox = (DatePicker) this.findViewById(R.id.add_homework_datepicker);

        this.mSubjectNameBox.setAdapter(new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, this.mSubjects));

        int index = this.mSubjects.indexOf(this.mSelectedHomework.getSubject());
        this.mSubjectNameBox.setSelection(index);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.mSelectedHomework.getSubmitDate());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month =  calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        this.mSubmitDateBox.updateDate(year, month, day);
    }

    private void getSpinnerData() {
        final String accessToken = this.mAccessToken;
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponseHelper response = DataPersister.GetAllSubjects(accessToken);
                EditHomeworkActivity.this.handleGetAllSubjectsResponse(response);
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
                    EditHomeworkActivity.this.handleAddHomeworkResponse(response);
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

    private void handleGetAllSubjectsResponse(HttpResponseHelper response) {
        SubjectModel[] subjectModels;
        if (response.isStatusOk()) {
            subjectModels = this.mGson.fromJson(response.getMessage(), SubjectModel[].class);
            this.mSubjects = SubjectModel.getNames(subjectModels);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EditHomeworkActivity.this.setupFields();
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
        int day = this.mSubmitDateBox.getDayOfMonth();
        int month = this.mSubmitDateBox.getMonth();
        int year = this.mSubmitDateBox.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        String subjectName = this.mSubjects.get(this.mSubjectNameBox.getSelectedItemPosition());
        Date submitDate = calendar.getTime();

        HomeworkModel homeworkModel = new HomeworkModel(subjectName, submitDate);

        return homeworkModel;
    }
}