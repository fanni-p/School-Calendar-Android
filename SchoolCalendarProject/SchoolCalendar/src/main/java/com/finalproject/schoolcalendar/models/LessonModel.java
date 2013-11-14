package com.finalproject.schoolcalendar.models;

import java.sql.Time;

/**
 * Created by Fani on 11/14/13.
 */
public class LessonModel {
    private int id;
    private String subject;
    private String subjectColor;
    private String day;
    private int dayNumber;
    private String startTime;
    private String endTime;
    private LessonType type;
    private String room;
    private String note;

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
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
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

//    public string StartTimeString
//    {
//        get
//        {
//            return this.StartTime.ToString(@"hh\:mm");
//        }
//    }
//
//    public string EndTimeString
//    {
//        get
//        {
//            return this.EndTime.ToString(@"hh\:mm");
//        }
//    }
//
//    public string LessonRoom
//    {
//        get
//        {
//            if (this.Room == "0" || String.IsNullOrEmpty(this.Room))
//            {
//                return "No room";
//            }
//
//            return this.Room.ToString();
//        }
//    }
}
