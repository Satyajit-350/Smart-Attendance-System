package com.example.smartattendance.Model;

import java.util.ArrayList;
import java.util.List;

public class Users {

    private String profile_pic,userName,userId,email,phone,password,department,about;
    private List<String> courseIds;

    public Users(String profile_pic, String userName, String userId, String email,
                 String phone, String password, String department, String about) {
        this.profile_pic = profile_pic;
        this.userName = userName;
        this.userId = userId;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.department = department;
        this.about = about;
    }

    public Users(){

    }

    public Users(String userName, String email, String phone, String password) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.courseIds = new ArrayList<>();
    }

    public Users(String email, String userName, List<String> courseIds){
        this.email = email;
        this.userName = userName;
        this.courseIds = courseIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<String> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<String> courseIds) {
        this.courseIds = courseIds;
    }
}
