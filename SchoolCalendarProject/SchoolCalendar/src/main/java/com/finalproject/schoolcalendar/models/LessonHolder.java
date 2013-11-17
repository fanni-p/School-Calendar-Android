package com.finalproject.schoolcalendar.models;

import android.view.View;
import android.widget.TextView;

import com.finalproject.schoolcalendar.R;

/**
 * Created by Fani on 11/17/13.
 */
public class LessonHolder {
    public TextView lesson_title;
    public TextView lesson_type;
    public TextView lesson_room;
    public TextView lesson_start;
    public TextView lesson_end;

    public LessonHolder(View row){
        this.lesson_title = (TextView) row.findViewById(R.id.lesson_title);
        this.lesson_type = (TextView) row.findViewById(R.id.lesson_type);
        this.lesson_room = (TextView) row.findViewById(R.id.lesson_room);
        this.lesson_start = (TextView) row.findViewById(R.id.lesson_start);
        this.lesson_end = (TextView) row.findViewById(R.id.lesson_end);
    }
}
