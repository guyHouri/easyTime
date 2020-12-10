package com.example.easytime101;

import android.app.PendingIntent;

import java.text.DecimalFormat;

public class modelAlarm {
    private String time;
    private long cal;
    private String name;
    private String whatDaysRepeated;
    private String date;
    private boolean hasHappened;
    private boolean hasStopped;
    private Long timeInMillies;
    private int dateTimeInNumber; // date will be first the months and than the days, for instance 16:20 may 16 2020 wiil be 20205161620

    public modelAlarm() {
        this.time="";
        this.name="";
        this.date="";
        this.whatDaysRepeated="";
        this.hasHappened=false;
        this.hasStopped=false;
    }

    public boolean getHasStopped() {
        return hasStopped;
    }
    public void setHasStopped(boolean hasStopped) {
        this.hasStopped = hasStopped;
    }

    public Long getTimeInMillies() {
        return timeInMillies;
    }
    public void setTimeInMillies(Long timeInMillies) {
        this.timeInMillies = timeInMillies;
    }

    public long getCal() {
        return cal;
    }
    public void setCal(long cal) {
        this.cal = cal;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getWhatDaysRepeated() {
        return whatDaysRepeated;
    }
    public void setWhatDaysRepeated(String whatDaysRepeated) {
        this.whatDaysRepeated = whatDaysRepeated;
    }

    public int getDateTimeInNumber() {
        return dateTimeInNumber;
    }
    public void setDateTimeInNumber(int dateInNumber) {
        this.dateTimeInNumber = dateInNumber;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public boolean getHasHappened() {
        return hasHappened;
    }
    public void setHasHappened(boolean on) {
        this.hasHappened = on;
    }
}
