package com.afeka.whereapp.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private List<String> groups;

    public User(String id, String name, String email, List<String> groups) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.groups = new ArrayList<String>();
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public String toString() {
        return "User: {id: " + id + ", name: " + name + ", email: " + email;
    }
}
