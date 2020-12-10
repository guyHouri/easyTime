package com.example.easytime101;

import android.provider.ContactsContract;

public class UsersList {

    private String username;
    private String password;
    private String email;
    private boolean stayLoggedIn;

    public UsersList(String username, String password, String email) {
        this.username = username;
        this.password=password;
        this.email=email;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStayLoggedIn() {
        return stayLoggedIn;
    }
    public void setStayLoggedIn(boolean stayLoggedIn) {
        this.stayLoggedIn = stayLoggedIn;
    }
}
