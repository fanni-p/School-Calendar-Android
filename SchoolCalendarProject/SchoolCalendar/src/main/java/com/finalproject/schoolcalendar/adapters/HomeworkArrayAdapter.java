package com.finalproject.schoolcalendar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.models.HomeworkModel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Fani on 11/16/13.
 */
public class HomeworkArrayAdapter extends ArrayAdapter<HomeworkModel> {
    private int mResourceId;
    private Context mContext;
    private HomeworkModel[] mHomeworkObjects;

    public HomeworkArrayAdapter(Context context, int resourceId, HomeworkModel[] homeworkObjects) {
        super(context, resourceId, homeworkObjects);

        this.mContext = context;
        this.mResourceId = resourceId;
        this.mHomeworkObjects = homeworkObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HomeworkHolder homeworkHolder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(this.mResourceId, parent, false);

            homeworkHolder = new HomeworkHolder();
            if (row != null) {
                homeworkHolder.homework_subject = (TextView) row.findViewById(R.id.homework_subject);
                homeworkHolder.homework_submitdate = (TextView) row.findViewById(R.id.homework_submitdate);
                homeworkHolder.homework_daysleft = (TextView) row.findViewById(R.id.homework_daysleft);
                homeworkHolder.homework_daysleft_string = (TextView) row.findViewById(R.id.homework_daysleft_string);

                row.setTag(homeworkHolder);
            }
        } else {
            homeworkHolder = (HomeworkHolder) row.getTag();
        }

        HomeworkModel homework = this.mHomeworkObjects[position];
        long daysleft = daysBetween(homework.getSubmitDate());
        if(daysleft > 1){
            homeworkHolder.homework_subject.setTextColor(Color.GREEN);
            homeworkHolder.homework_submitdate.setTextColor(Color.GREEN);
            homeworkHolder.homework_daysleft.setTextColor(Color.GREEN);
            homeworkHolder.homework_daysleft_string.setTextColor(Color.GREEN);
        } else {
            homeworkHolder.homework_subject.setTextColor(Color.RED);
            homeworkHolder.homework_submitdate.setTextColor(Color.RED);
            homeworkHolder.homework_daysleft.setTextColor(Color.RED);
            homeworkHolder.homework_daysleft_string.setTextColor(Color.RED);
        }

        homeworkHolder.homework_subject.setText(homework.getSubject());
        homeworkHolder.homework_submitdate.setText(DateFormat.format("dd/MM/yyyy", homework.getSubmitDate()));
        homeworkHolder.homework_daysleft.setText(String.valueOf(daysleft));

        return row;
    }

    public long daysBetween(Date endDate) {
        Calendar startDate = Calendar.getInstance();
        Calendar date = (Calendar) startDate.clone();
        Calendar end = new GregorianCalendar();
        end.setTime(endDate);

        int daysBetween = 0;
        while (date.before(end)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }

        return daysBetween;
    }

    public static class HomeworkHolder {
        TextView homework_subject;
        TextView homework_submitdate;
        TextView homework_daysleft;
        TextView homework_daysleft_string;
    }
}
