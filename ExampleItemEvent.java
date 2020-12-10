package com.example.easytime101;

public class ExampleItemEvent {

    private String title;
    private int mImageResource;
    private String date;
    private int id;
    private String startTime;
    private String place;

    public ExampleItemEvent(String tittle, int mImageResource, String date, String startTime, String place) {
        this.title = tittle;
        this.mImageResource = mImageResource;
        this.date = date;
        this.startTime = startTime;
        this.place = place;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String tittle) {
        this.title = tittle;
    }

    public int getmImageResource() {
        return mImageResource;
    }
    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
}
