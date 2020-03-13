package com.afeka.whereapp.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class Group {

    private String id;
    private String name;
    private Location location;

    public Group() {
    }

    public Group(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @NonNull
    @Override
    public String toString() {
        String str = "Group: {id: " + id +", name: " + name;
        if (location == null) {
            return str + "}";
        }
        return  str + location.toString() + "}";
    }
}
