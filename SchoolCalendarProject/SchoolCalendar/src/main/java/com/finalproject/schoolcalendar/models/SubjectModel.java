package com.finalproject.schoolcalendar.models;

/**
 * Created by Fani on 11/16/13.
 */
public class SubjectModel {
    private int id;
    private String name;
    private String teacher;
    private String color;

    public SubjectModel(int id, String name, String teacher, String color) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
