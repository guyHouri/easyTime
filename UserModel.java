
 package com.example.easytime101;

import java.util.List;

public class UserModel {

    Boolean urgent, important, isEnded;
    String username, date, whatWas;
    int tag, firstTag;
    int imageResource;
    int titleResource;

    public UserModel(Boolean urgent, String username, int tag, Boolean important, String date) {
        this.urgent=urgent;
        this.username = username;
        this.tag=tag;
        this.important=important;
        this.date=date;
        this.isEnded=true;
        imageResource = R.drawable.check;
        titleResource = R.drawable.login_shape;
    }

    public String getWhatWas() {
        return whatWas;
    }
    public void setWhatWas(String whatWas) {
        this.whatWas = whatWas;
    }

    public int getTitleResource() {
        return titleResource;
    }
    public void setTitleResource(int titleResourceChecked) {
        this.titleResource = titleResourceChecked;
    }

    public int getImageResource() {
        return imageResource;
    }
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public Boolean getEnded() {
        return isEnded;
    }
    public void setEnded(Boolean ended) {
        isEnded = ended;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getImportant() {
        return important;
    }
    public void setImportant(Boolean important) {
        this.important = important;
    }

    public Boolean getUrgent() {
        return urgent;
    }
    public void setUrgent(Boolean urgent) {
        this.urgent = urgent;
    }

    public int getTag() {
        return tag;
    }
    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }


}