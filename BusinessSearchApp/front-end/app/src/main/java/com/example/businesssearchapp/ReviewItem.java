package com.example.businesssearchapp;

public class ReviewItem {
    String username;
    int userrating;
    String usertext;
    String usertime;

    public ReviewItem(String username, int userrating, String usertext, String usertime) {
        this.username = username;
        this.userrating = userrating;
        this.usertext = usertext;
        this.usertime = usertime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserrating() {
        return userrating;
    }

    public void setUserrating(int userrating) {
        this.userrating = userrating;
    }

    public String getUsertext() {
        return usertext;
    }

    public void setUsertext(String usertext) {
        this.usertext = usertext;
    }

    public String getUsertime() {
        return usertime;
    }

    public void setUsertime(String usertime) {
        this.usertime = usertime;
    }
}

