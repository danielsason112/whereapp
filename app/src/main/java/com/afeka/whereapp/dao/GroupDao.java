package com.afeka.whereapp.dao;

import com.afeka.whereapp.data.Group;

import java.util.List;

public interface GroupDao {
    Group create(Group group);
    void readAll(OnResponse res);
    void delete(Group group);
    void deleteAll();
}
