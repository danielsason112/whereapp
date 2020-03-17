package com.afeka.whereapp.dao;

import com.afeka.whereapp.data.User;
import com.google.firebase.database.DataSnapshot;

public interface UserDao {

    User create(User user, OnResponse<User> res);
    void getUserById(String id, OnResponse<DataSnapshot> data);
    void addGroupById(String userId, String groupId);
    void getTokenById(String userId, OnResponse<DataSnapshot> res);
    void updateTokenById(String userId, String token);
}
