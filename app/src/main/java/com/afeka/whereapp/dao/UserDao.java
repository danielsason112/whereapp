package com.afeka.whereapp.dao;

import com.afeka.whereapp.data.User;
import com.google.firebase.database.DataSnapshot;

public interface UserDao {

    User create(User user);
    void getUserById(String id, OnResponse<DataSnapshot> data);
}
