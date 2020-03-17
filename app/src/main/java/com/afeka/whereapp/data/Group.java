package com.afeka.whereapp.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class Group {

    private String id;
    private String name;
    private Location location;
    private String description;

    public Group() {
    }

    public Group(String id, String name, Location location, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
