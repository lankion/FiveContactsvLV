package com.example.fivecontacts.main.model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String name;
    String login;
    String password;
    String email;
    boolean stayedConnected = false;
    boolean darkTheme = false;
    ArrayList<Contact> contacts;

    public User(String name, String login, String password, String email, boolean stayedConnected, boolean darkTheme) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.stayedConnected = stayedConnected;
        this.contacts = new ArrayList<Contact>();
    }

    public User() {
        this.contacts = new ArrayList<Contact>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public boolean isStayedConnected() {
        return stayedConnected;
    }

    public void setStayedConnected(boolean stayedConnected) {
        this.stayedConnected = stayedConnected;
    }

    public boolean isDarkTheme() {
        return darkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

}
