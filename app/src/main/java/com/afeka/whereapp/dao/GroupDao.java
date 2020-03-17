package com.afeka.whereapp.dao;

import com.afeka.whereapp.data.Group;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public interface GroupDao {
    Group create(Group group);
    void readAll(OnResponse<DataSnapshot> res);
    void readById(String id, OnResponse<DataSnapshot> res);
    void delete(Group group);
    void deleteAll();
}
