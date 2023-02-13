package com.example.businesssearchapp;

public class Item {

    int idNum;
    String imgUrl;
    String busiName;
    double busiRating;
    double busiDistance;
    String id;

    public Item(int idNum, String imgUrl, String busiName, double busiRating, double busiDistance, String id) {
        this.idNum = idNum;
        this.imgUrl = imgUrl;
        this.busiName = busiName;
        this.busiRating = busiRating;
        this.busiDistance = busiDistance;
        this.id = id;
    }

    public int getIdNum() {
        return idNum;
    }

    public void setIdNum(int idNum) {
        this.idNum = idNum;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBusiName() {
        return busiName;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }

    public double getBusiRating() {
        return busiRating;
    }

    public void setBusiRating(double busiRating) {
        this.busiRating = busiRating;
    }

    public double getBusiDistance() {
        return busiDistance;
    }

    public void setBusiDistance(double busiDistance) {
        this.busiDistance = busiDistance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
