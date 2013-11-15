package com.finalproject.schoolcalendar.models;

import com.finalproject.schoolcalendar.enums.LessonType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fani on 11/14/13.
 */
public class LessonModel {
    private int id;
    private int dayNumber;
    private String day;
    private String room;
    private String note;
    private String subject;
    private String endTime;
    private String startTime;
    private String subjectColor;
    private LessonType type;
    private Date date;
    private SimpleDateFormat uiTimeFormat;
    private SimpleDateFormat jsonTimeFormat;

    public LessonModel(int id, String subject, String subjectColor, String day,
                       int dayNumber, String startTime, String endTime, LessonType type, String room, String note) {
        this.id = id;
        this.subject = subject;
        this.subjectColor = subjectColor;
        this.day = day;
        this.dayNumber = dayNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.room = room;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectColor() {
        return subjectColor;
    }

    public void setSubjectColor(String subjectColor) {
        this.subjectColor = subjectColor;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getStartTime() {
        if (this.uiTimeFormat == null) {
            this.uiTimeFormat = new SimpleDateFormat("hh:mm aa");
        }

        try {
            if (this.jsonTimeFormat == null) {
                this.jsonTimeFormat = new SimpleDateFormat("HH:mm:ss");
            }

            this.date = this.jsonTimeFormat.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return this.uiTimeFormat.format(date);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        if (this.uiTimeFormat == null) {
            this.uiTimeFormat = new SimpleDateFormat("hh:mm aa");
        }

        try {
            if (this.jsonTimeFormat == null) {
                this.jsonTimeFormat = new SimpleDateFormat("HH:mm:ss");
            }

            this.date = this.jsonTimeFormat.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return this.uiTimeFormat.format(date);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public LessonType getType() {
        return type;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
