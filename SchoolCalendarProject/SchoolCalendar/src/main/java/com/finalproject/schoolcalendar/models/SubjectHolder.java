package com.finalproject.schoolcalendar.models;

import android.view.View;
import android.widget.TextView;

import com.finalproject.schoolcalendar.R;

/**
 * Created by Fani on 11/17/13.
 */
public class SubjectHolder {
    public TextView subject_name;
    public TextView subject_color;

    public SubjectHolder(View row){
        this.subject_name = (TextView) row.findViewById(R.id.subject_name);
        this.subject_color = (TextView) row.findViewById(R.id.subject_color);
    }
}
