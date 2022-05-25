package com.example.smartattendance.Model;

public class StudentModel {

    private String sName;
    private String sDate;
    private String sTime;

    public StudentModel(String sName, String sDate, String sTime) {
        this.sName = sName;
        this.sDate = sDate;
        this.sTime = sTime;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }
}
