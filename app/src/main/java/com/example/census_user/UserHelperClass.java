package com.example.census_user;

public class UserHelperClass {
    String Name = "", Mobile_No = "",state="enabled" ,post="";
    public UserHelperClass() {
    }

    public UserHelperClass(String name, String mobile_No ,String post) {
        Name = name;
        Mobile_No = mobile_No;
        this.post = post;
    }
    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile_No() {
        return Mobile_No;
    }

    public void setMobile_No(String mobile_No) {
        Mobile_No = mobile_No;
    }
}