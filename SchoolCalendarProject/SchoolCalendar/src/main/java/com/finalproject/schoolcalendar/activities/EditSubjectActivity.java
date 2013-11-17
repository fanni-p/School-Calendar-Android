package com.finalproject.schoolcalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.data.DataPersister;
import com.finalproject.schoolcalendar.data.HttpResponseHelper;
import com.finalproject.schoolcalendar.enums.ColorEnum;
import com.finalproject.schoolcalendar.helpers.ColorConverter;
import com.finalproject.schoolcalendar.helpers.SessionManager;
import com.finalproject.schoolcalendar.models.SubjectModel;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by Fani on 11/17/13.
 */
public class EditSubjectActivity extends Activity {

    private static final String SELECTED_SUBJECT = "SelectedSubject";

    private Handler mHandler;
    private ColorEnum mColor;
    private Spinner mColorBox;
    private String mTeacher;
    private String mSubjectName;
    private String mAccessToken;
    private EditText mTeacherBox;
    private EditText mSubjectNameBox;
    private SubjectModel mSubjectModel;
    private SubjectModel mSelectedSubject;
    private HandlerThread mHandledThread;

    private int mCurrentSelection;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        Gson gson = new Gson();
        Intent intent = getIntent();
        String selectedSubject = intent.getStringExtra(SELECTED_SUBJECT);
        this.mSelectedSubject = gson.fromJson(selectedSubject, SubjectModel.class);

        this.mColor = null;
        this.mTeacher = null;
        this.mSubjectName = null;

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        this.mAccessToken = user.get(SessionManager.KEY_ACCESSTOKEN);

        this.mHandledThread = new HandlerThread("AddSubjectServiceThread");
        this.mHandledThread.start();

        Looper looper = this.mHandledThread.getLooper();
        if (looper != null) {
            this.mHandler = new Handler(looper);
        }

        this.setupSpinner();
        this.setupFields();
        this.setupCreateButton();
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

    private void setupSpinner() {
        final Spinner spinner = (Spinner) findViewById(R.id.add_subject_color_spinner);
        spinner.setAdapter(new ArrayAdapter<ColorEnum>
                (this, android.R.layout.simple_spinner_item, ColorEnum.values()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (EditSubjectActivity.this.mCurrentSelection != position) {
                    ColorEnum selection = (ColorEnum) spinner.getItemAtPosition(position);

                    if (selection != null) {
                        String convertedColor = ColorConverter.ParseColor(selection.name());
                        int color = Color.parseColor(convertedColor);
                        spinner.setBackgroundColor(color);
                    }

                    EditSubjectActivity.this.mCurrentSelection = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    private void setupFields() {
        TextView pageName = (TextView) this.findViewById(R.id.addsubject_pageName);
        pageName.setText(R.string.edit_subject_page_name);

        int enumIndex = ColorEnum.valueOf(this.mSelectedSubject.getColor()).ordinal();
        String convertedColor = ColorConverter.ParseColor
                (ColorEnum.valueOf(this.mSelectedSubject.getColor()).toString());
        int color = Color.parseColor(convertedColor);

        this.mColorBox= (Spinner) this.findViewById(R.id.add_subject_color_spinner);
        this.mTeacherBox = (EditText) this.findViewById(R.id.add_subject_new_teacher);
        this.mSubjectNameBox = (EditText) this.findViewById(R.id.add_subject_new_subjectname);

        this.mSubjectNameBox.setText(this.mSelectedSubject.getName());
        this.mTeacherBox.setText(this.mSelectedSubject.getTeacher());
        this.mColorBox.setSelection(enumIndex);
        this.mColorBox.setBackgroundColor(color);
    }

    private void setupCreateButton() {
        Button editButton = (Button) this.findViewById(R.id.add_subject_createbutton);
        editButton.setText(R.string.edit_subject_editbutton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditSubjectActivity.this.handleCreateButtonCommand();
            }
        });
    }

    private void handleCreateButtonCommand() {
        final int id = this.mSelectedSubject.getId();
        final String accessToken = this.mAccessToken;
        final SubjectModel subjectModel = this.createSubjectModel();

        if (subjectModel != null) {
            this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    HttpResponseHelper response = DataPersister.EditSubject(subjectModel, accessToken, id);
                    EditSubjectActivity.this.handleEditSubjectResponse(response);
                }
            });
        }
    }

    private void handleEditSubjectResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            Intent intent = new Intent(this, AllSubjectsActivity.class);
            this.startActivity(intent);
            finish();
        }
    }

    private SubjectModel createSubjectModel() {
        this.mSubjectName = this.mSubjectNameBox.getText().toString();
        this.mTeacher = this.mTeacherBox.getText().toString();
        this.mColor = (ColorEnum) this.mColorBox.getSelectedItem();

        String color = null;
        if (this.mColor != null) {
            color = this.mColor.name();
        }

        if (this.mSubjectName != null) {
            this.mSubjectModel = new SubjectModel(this.mSubjectName, this.mTeacher, color);
        }

        return this.mSubjectModel;
    }
}