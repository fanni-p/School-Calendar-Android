package com.finalproject.schoolcalendar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.finalproject.schoolcalendar.R;
import com.finalproject.schoolcalendar.helpers.ColorConverter;
import com.finalproject.schoolcalendar.models.LessonHolder;
import com.finalproject.schoolcalendar.models.LessonModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Fani on 11/17/13.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mListDataHeader;
    private HashMap<String, List<LessonModel>> mListDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<LessonModel>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final LessonModel lesson = (LessonModel) this.getChild(groupPosition, childPosition);

//        if (convertView == null) {
//            LayoutInflater infalInflater = (LayoutInflater) this.mContext
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = infalInflater.inflate(R.layout.list_item, null);
//        }
        View row = convertView;
        LessonHolder lessonHolder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item, null);

            String convertedColor = ColorConverter.ParseColor(lesson.getSubjectColor());
            int color = Color.parseColor(convertedColor);

            lessonHolder = new LessonHolder(row);
            if (row != null) {
                row.setBackgroundColor(R.attr.background);
                if (color != 0) {
                    row.setBackgroundColor(color);
                }

                row.setTag(lessonHolder);
            }
        } else {
            lessonHolder = (LessonHolder) row.getTag();
        }

        lessonHolder.lesson_title.setText(lesson.getSubject());
        lessonHolder.lesson_type.setText(lesson.getType().toString());
        lessonHolder.lesson_start.setText(DateFormat.format("hh:mm aa", lesson.getStartTime()));
        lessonHolder.lesson_end.setText(DateFormat.format("hh:mm aa", lesson.getEndTime()));
        if(lesson.getRoom().equals("0")){
            lessonHolder.lesson_room.setText(this.mContext.getString(R.string.home_no_room_string));
        } else {
            lessonHolder.lesson_room.setText(lesson.getRoom());
        }

        return row;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) this.getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater =
                    (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView listHeaderBox = (TextView) convertView.findViewById(R.id.list_header);
        listHeaderBox.setTypeface(null, Typeface.BOLD);
        listHeaderBox.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
