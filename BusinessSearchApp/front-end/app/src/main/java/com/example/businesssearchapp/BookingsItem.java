package com.example.businesssearchapp;

public class BookingsItem {
    String id;
    String resNameTxt;
    String resDateTxt;
    String resTimeTxt;
    String resEmailTxt;

    public BookingsItem(String id, String resNameTxt, String resDateTxt, String resTimeTxt, String resEmailTxt) {
        this.id = id;
        this.resNameTxt = resNameTxt;
        this.resDateTxt = resDateTxt;
        this.resTimeTxt = resTimeTxt;
        this.resEmailTxt = resEmailTxt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResNameTxt() {
        return resNameTxt;
    }

    public void setResNameTxt(String resNameTxt) {
        this.resNameTxt = resNameTxt;
    }

    public String getResDateTxt() {
        return resDateTxt;
    }

    public void setResDateTxt(String resDateTxt) {
        this.resDateTxt = resDateTxt;
    }

    public String getResTimeTxt() {
        return resTimeTxt;
    }

    public void setResTimeTxt(String resTimeTxt) {
        this.resTimeTxt = resTimeTxt;
    }

    public String getResEmailTxt() {
        return resEmailTxt;
    }

    public void setResEmailTxt(String resEmailTxt) {
        this.resEmailTxt = resEmailTxt;
    }
}
