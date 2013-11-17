package com.finalproject.schoolcalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.data.DataPersister;
import com.finalproject.schoolcalendar.data.HttpResponseHelper;
import com.finalproject.schoolcalendar.enums.ColorEnum;
import com.finalproject.schoolcalendar.enums.DaysOfWeek;
import com.finalproject.schoolcalendar.enums.LessonType;
import com.finalproject.schoolcalendar.helpers.SessionManager;
import com.finalproject.schoolcalendar.models.LessonModel;
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
public class AddLessonActivity extends Activity {

    private Handler mHandler;
    private ColorEnum mColor;
    private String mAccessToken;
    private ArrayList<String> mSubjects;
    private SubjectModel mSubjectModel;
    private HandlerThread mHandledThread;
    private Gson mGson;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);

        this.mGson = new GsonBuilder().setDateFormat("HH:mm:ss").create();

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        this.mAccessToken = user.get(SessionManager.KEY_ACCESSTOKEN);

        this.mHandledThread = new HandlerThread("AddLessonServiceThread");
        this.mHandledThread.start();

        Looper looper = this.mHandledThread.getLooper();
        if (looper != null) {
            this.mHandler = new Handler(looper);
        }

        this.setupAddButton();
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

    private void setupAddButton() {
        Button loginButton = (Button) this.findViewById(R.id.add_lesson_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddLessonActivity.this.handleAddButtonCommand();
            }
        });
    }

    private void setupSpinner() {
        Spinner daySpinner = (Spinner) findViewById(R.id.day_spinner);
        daySpinner.setAdapter(new ArrayAdapter<DaysOfWeek>
                (this, android.R.layout.simple_spinner_item, DaysOfWeek.values()));

        Spinner typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        typeSpinner.setAdapter(new ArrayAdapter<LessonType>
                (this, android.R.layout.simple_spinner_item, LessonType.values()));

        Spinner spinner = (Spinner) findViewById(R.id.subject_spinner);
        spinner.setAdapter(new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, this.mSubjects));
    }

    private void getSpinnerData() {
        final String accessToken = this.mAccessToken;
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                HttpResponseHelper response = DataPersister.GetAllSubjects(accessToken);
                AddLessonActivity.this.handleGetAllSubjectsResponse(response);
            }
        });
    }

    private void handleAddButtonCommand() {
        final String accessToken = this.mAccessToken;
        final LessonModel lessonModel = this.createSubjectModel();

        if (lessonModel != null) {
            this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    HttpResponseHelper response = DataPersister.AddNewLesson(lessonModel, accessToken);
                    AddLessonActivity.this.handleAddSubjectResponse(response);
                }
            });
        }
    }

    private void handleGetAllSubjectsResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            SubjectModel[] mSubjectsModels = this.mGson.fromJson(response.getMessage(), SubjectModel[].class);
            this.mSubjects = SubjectModel.getNames(mSubjectsModels);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AddLessonActivity.this.setupSpinner();
                }
            });
        }
    }

    private void handleAddSubjectResponse(HttpResponseHelper response) {
        if (response.isStatusOk()) {
            Intent intent = new Intent(this, HomeActivity.class);
            this.startActivity(intent);
            finish();
        }
    }

    private LessonModel createSubjectModel() {
        Spinner subjectNameBox = (Spinner) this.findViewById(R.id.subject_spinner);
        Spinner typeBox = (Spinner) this.findViewById(R.id.type_spinner);
        Spinner dayBox = (Spinner) this.findViewById(R.id.day_spinner);
        TimePicker startTimeBox = (TimePicker) this.findViewById(R.id.start_time_picker);
        TimePicker endTimeBox = (TimePicker) this.findViewById(R.id.end_time_picker);
        EditText roomBox = (EditText) this.findViewById(R.id.room);
        EditText noteBox = (EditText) this.findViewById(R.id.note);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, startTimeBox.getCurrentHour());
        calendar.set(Calendar.MINUTE, startTimeBox.getCurrentMinute());
        Date start = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, endTimeBox.getCurrentHour());
        calendar.set(Calendar.MINUTE, endTimeBox.getCurrentMinute());
        Date end = calendar.getTime();

        String subject = this.mSubjects.get(subjectNameBox.getSelectedItemPosition());
        DaysOfWeek selectedItem = (DaysOfWeek) dayBox.getSelectedItem();
        String day = selectedItem.name();
        LessonType selectedType = (LessonType) typeBox.getSelectedItem();
        String room = roomBox.getText().toString();
        String note = noteBox.getText().toString();

        LessonModel lessonModel = new LessonModel(subject, day, start, end, selectedType, room, note);

        return lessonModel;
    }
}