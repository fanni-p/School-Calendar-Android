package com.finalproject.schoolcalendar.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.helpers.ColorConverter;
import com.finalproject.schoolcalendar.models.LessonModel;

/**
 * Created by Fani on 11/14/13.
 */
public class LessonsArrayAdapter extends ArrayAdapter<LessonModel> {

    private int mResourceId;
    private Context mContext;
    private LessonModel[] mLessonsObjects;

    public LessonsArrayAdapter(Context context, int resourceId, LessonModel[] lessonsObjects) {
        super(context, resourceId, lessonsObjects);

        this.mContext = context;
        this.mResourceId = resourceId;
        this.mLessonsObjects = lessonsObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LessonHolder lessonHolder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(this.mResourceId, parent, false);

            String convertedColor = ColorConverter.ParseColor(this.mLessonsObjects[position].getSubjectColor());
            int color = Color.parseColor(convertedColor);

            lessonHolder = new LessonHolder();
            if (row != null) {
                if (color != 0) {
                    row.setBackgroundColor(color);
                }

                lessonHolder.lesson_title = (TextView) row.findViewById(R.id.lesson_title);
                lessonHolder.lesson_type = (TextView) row.findViewById(R.id.lesson_type);
                lessonHolder.lesson_room = (TextView) row.findViewById(R.id.lesson_room);
                lessonHolder.lesson_start = (TextView) row.findViewById(R.id.lesson_start);
                lessonHolder.lesson_end = (TextView) row.findViewById(R.id.lesson_end);

                row.setTag(lessonHolder);
            }
        } else {
            lessonHolder = (LessonHolder) row.getTag();
        }

        LessonModel lesson = this.mLessonsObjects[position];
        lessonHolder.lesson_title.setText(lesson.getSubject());
        lessonHolder.lesson_type.setText(lesson.getType().toString());
        lessonHolder.lesson_start.setText(DateFormat.format("hh:mm aa", lesson.getStartTime()));
        lessonHolder.lesson_end.setText(DateFormat.format("hh:mm aa", lesson.getEndTime()));
        if(lesson.getRoom().equals("0")){
            lessonHolder.lesson_room.setText(this.mContext.getString(R.string.home_noroom_string));
        } else {
            lessonHolder.lesson_room.setText(lesson.getRoom());
        }

        return row;
    }

    public static class LessonHolder {
        TextView lesson_title;
        TextView lesson_type;
        TextView lesson_room;
        TextView lesson_start;
        TextView lesson_end;
    }
}
