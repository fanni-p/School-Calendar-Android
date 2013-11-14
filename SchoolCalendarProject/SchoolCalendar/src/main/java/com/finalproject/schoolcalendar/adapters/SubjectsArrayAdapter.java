package com.finalproject.schoolcalendar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.helpers.ColorConverter;
import com.finalproject.schoolcalendar.models.SubjectModel;

/**
 * Created by Fani on 11/16/13.
 */
public class SubjectsArrayAdapter extends ArrayAdapter<SubjectModel> {

    private int mResourceId;
    private Context mContext;
    private SubjectModel[] mSubjectsObjects;

    public SubjectsArrayAdapter(Context context, int resourceId, SubjectModel[] subjectsObjects) {
        super(context, resourceId, subjectsObjects);

        this.mContext = context;
        this.mResourceId = resourceId;
        this.mSubjectsObjects = subjectsObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SubjectHolder subjectHolder;
        int subjectColor = 0;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(this.mResourceId, parent, false);

            String convertedColor = ColorConverter.ParseColor(this.mSubjectsObjects[position].getColor());
            subjectColor = Color.parseColor(convertedColor);

            subjectHolder = new SubjectHolder();
            if (row != null) {
                subjectHolder.subject_name = (TextView) row.findViewById(R.id.subject_name);
                subjectHolder.subject_color = (TextView) row.findViewById(R.id.subject_color);

                row.setTag(subjectHolder);
            }
        } else {
            subjectHolder = (SubjectHolder) row.getTag();
        }

        SubjectModel subject = this.mSubjectsObjects[position];
        subjectHolder.subject_name.setText(subject.getName());
        if (subjectColor != 0) {
            subjectHolder.subject_color.setBackgroundColor(subjectColor);
        }

        return row;
    }

    public static class SubjectHolder {
        TextView subject_name;
        TextView subject_color;
    }
}
