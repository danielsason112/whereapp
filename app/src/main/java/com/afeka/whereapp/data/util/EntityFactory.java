package com.afeka.whereapp.data.util;

import com.afeka.whereapp.data.Group;
import com.afeka.whereapp.data.IdGenerator;
import com.afeka.whereapp.data.Location;
import com.afeka.whereapp.data.User;

import java.util.Map;

public class EntityFactory {

    // Create a new Group entity. Passing a null id will Generate a new id
    public Group createNewGroup(String id, String name, double lat, double lng, String description) {
        if (id == null) {
            id = IdGenerator.generateId();
        }

        return new Group(id, name, new Location(lat, lng), description);
    }

    public Group createNewGroup(String id, Group group) {
        if (group == null) {
            return null;
        }
        group.setId(id);
        return group;
    }

    public User createNewUser(String id, String name, String email, Map<String, Boolean> groups, String token) {
        return new User(id, name, email, groups, token);
    }

    public User createNewUser(String id, User user) {
        if (user == null) {
            return null;
        }
        user.setId(id);
        return user;
    }
}
