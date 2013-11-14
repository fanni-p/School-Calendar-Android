package com.finalproject.schoolcalendar.models;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fani on 11/16/13.
 */
public class HomeworkModel {
    private int id;
    private boolean isDone;
    private String subject;
    private Date submitDate;
    private SimpleDateFormat uiTimeFormat;
    private SimpleDateFormat jsonTimeFormat;
    private String date;

    public HomeworkModel(int id, String subject, Date submitDate, boolean isDone) {
        this.id = id;
        this.subject = subject;
        this.submitDate = submitDate;
        this.isDone = isDone;
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

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

//    public string DateAsString
//    {
//        get
//        {
//            return this.SubmitDate.ToString("dd/MM/yyyy");
//        }
//    }
//
//    public int DaysLeft
//    {
//        get
//        {
//            var daysLeft = CalculateDaysLeft();
//            return daysLeft.Days;
//        }
//    }
//
//    public string ForegroundColor
//    {
//        get
//        {
//            if (this.DaysLeft <= 1)
//            {
//                return "Tomato";
//            }
//
//            return "LimeGreen";
//        }
//    }
//
//    private TimeSpan CalculateDaysLeft()
//    {
//        var days = this.SubmitDate - DateTime.Today;
//        return days;
//    }
}
