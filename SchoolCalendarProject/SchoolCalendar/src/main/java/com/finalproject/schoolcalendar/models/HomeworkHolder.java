package com.finalproject.schoolcalendar.models;

import android.view.View;
import android.widget.TextView;

import com.finalproject.schoolcalendar.R;

/**
 * Created by Fani on 11/17/13.
 */
public class HomeworkHolder {
    public TextView homework_subject;
    public TextView homework_submitdate;
    public TextView homework_daysleft;
    public TextView homework_daysleft_string;

    public HomeworkHolder(View row){
        this.homework_subject = (TextView) row.findViewById(R.id.homework_subject);
        this.homework_submitdate = (TextView) row.findViewById(R.id.homework_submitdate);
        this.homework_daysleft = (TextView) row.findViewById(R.id.homework_daysleft);
        this.homework_daysleft_string = (TextView) row.findViewById(R.id.homework_daysleft_string);
    }
}
