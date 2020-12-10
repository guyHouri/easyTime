package com.example.easytime101;

import java.util.ArrayList;

public class ListOfUsers {
    public static ArrayList<UsersList> listUsers;

    public ListOfUsers() {
    }
    public static ArrayList<UsersList> getListUsers() {
        return listUsers;
    }
    public static void setListUsers(ArrayList<UsersList> listUsers) {
        ListOfUsers.listUsers = listUsers;
    }
    public void addUser(UsersList usersList) {
        ListOfUsers.listUsers.add(usersList);
    }
}
