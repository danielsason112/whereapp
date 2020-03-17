package com.afeka.whereapp.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    private String id;
    private String name;
    private String email;
    private Map<String, Boolean> groups;
    private String token;

    public User() {
    }

    public User(String id, String name, String email, Map<String, Boolean> groups, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.groups = new HashMap<String, Boolean>();
        this.token = token;
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

    public Map<String, Boolean> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Boolean> groups) {
        this.groups = groups;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void addGroup(String groupId) {
        if (this.groups == null) {
            groups = new HashMap<String, Boolean>();
        }
        groups.put(groupId, true);
    }

    @NonNull
    @Override
    public String toString() {
        String groupsStr = "";
        if (groups != null) {
            groupsStr += groups.toString();
        }
        return "User: {id: " + id + ", name: " + name + ", email: " + email + ", groups: " + groupsStr + "}";
    }
}
