package com.example.myway.Model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userName;
    private String password;
    private String phoneNum;
    private String email;

    public User() {}

    public User(String n,String p, String phone,String e) {
        userName=n;
        password=p;
        phoneNum=phone;
        email=e;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put("userName", getUserName());
        json.put("password", getPassword());
        json.put("phone", getPhoneNum());
        json.put("email", getEmail());
        return json;
    }

    static User fromJson(Map<String,Object> json){
        String userName = (String)json.get("userName");
        if (userName == null){
            return null;
        }
        String password = (String)json.get("password");
        String phone = (String)json.get("phone");
        String email = (String)json.get("email");
        User user = new User(userName,password,phone,email);
        return user;
    }










}
