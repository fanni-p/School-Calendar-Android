package com.finalproject.schoolcalendar.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fani on 11/14/13.
 */
public enum LessonType {
    @SerializedName("0")
    Lecture,
    @SerializedName("1")
    Practice,
    @SerializedName("2")
    Exam,
    @SerializedName("3")
    Other
}
