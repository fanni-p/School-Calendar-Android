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
    private Date endTime;
    private Date startTime;
    private String subjectColor;
    private LessonType type;

    public LessonModel(int id, String subject, String subjectColor, String day,
                       int dayNumber, Date startTime, Date endTime, LessonType type, String room, String note) {
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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
