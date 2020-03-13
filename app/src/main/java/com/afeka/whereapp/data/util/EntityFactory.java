package com.afeka.whereapp.data.util;

import com.afeka.whereapp.data.Group;
import com.afeka.whereapp.data.IdGenerator;
import com.afeka.whereapp.data.Location;
import com.afeka.whereapp.data.User;

import java.util.ArrayList;

public class EntityFactory {

    // Create a new Group entity. Passing a null id will Generate a new id
    public Group createNewGroup(String id, String name, double lat, double lng) {
        if (id == null) {
            id = IdGenerator.generateId();
        }

        return new Group(id, name, new Location(lat, lng));
    }

    public User createNewUser(String id, String name, String email, String... groups) {
        ArrayList<String> list = new ArrayList<String>();
        for (String g : groups) {
            list.add(g);
        }
        return new User(id, name, email, list);
    }
}
