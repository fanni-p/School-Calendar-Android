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
public class AddSubjectActivity extends Activity {

    private Gson mGson;
    private Handler mHandler;
    private ColorEnum mColor;
    private String mTeacher;
    private String mSubjectName;
    private String mAccessToken;
    private int mCurrentSelection;
    private SubjectModel mSubjectModel;
    private HandlerThread mHandledThread;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        this.mGson = new Gson();
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

        this.setupCreateButton();
        this.setupSpinner();
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

    private void setupCreateButton() {
        Button loginButton = (Button) this.findViewById(R.id.add_subject_createbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSubjectActivity.this.handleCreateButtonCommand();
            }
        });
    }

    private void setupSpinner() {
        final Spinner spinner = (Spinner) findViewById(R.id.add_subject_color_spinner);
        spinner.setAdapter(new ArrayAdapter<ColorEnum>
                (this, android.R.layout.simple_spinner_item, ColorEnum.values()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (AddSubjectActivity.this.mCurrentSelection != position) {
                    ColorEnum selection = (ColorEnum) spinner.getItemAtPosition(position);

                    if (selection != null) {
                        String convertedColor = ColorConverter.ParseColor(selection.name());
                        int color = Color.parseColor(convertedColor);
                        spinner.setBackgroundColor(color);
                    }

                    AddSubjectActivity.this.mCurrentSelection = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    private void handleCreateButtonCommand() {
        final SubjectModel subjectModel = this.createSubjectModel();
        final String accessToken = this.mAccessToken;
        if (subjectModel != null) {
            this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    HttpResponseHelper response = DataPersister.AddNewSubject(subjectModel, accessToken);
                    AddSubjectActivity.this.handleAddSubjectResponse(response);
                }
            });
        }
    }

    private void handleAddSubjectResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            Intent intent = new Intent(this, AllSubjectsActivity.class);
            this.startActivity(intent);
            finish();
        }
    }

    private SubjectModel createSubjectModel() {
        EditText subjectNameBox = (EditText) this.findViewById(R.id.add_subject_new_subjectname);
        EditText teacherBox = (EditText) this.findViewById(R.id.add_subject_new_teacher);
        Spinner colorBox = (Spinner) this.findViewById(R.id.add_subject_color_spinner);

        this.mSubjectName = subjectNameBox.getText().toString();
        this.mTeacher = teacherBox.getText().toString();
        this.mColor = (ColorEnum) colorBox.getSelectedItem();

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