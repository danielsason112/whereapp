package com.afeka.whereapp.logic;

import android.content.Context;
import android.location.Location;

import androidx.room.Room;

import com.afeka.whereapp.AppDatabase;
import com.afeka.whereapp.dao.GroupDao;
import com.afeka.whereapp.dao.OnResponse;
import com.afeka.whereapp.dao.UserDao;
import com.afeka.whereapp.dao.firebase.FirebaseGroupDao;
import com.afeka.whereapp.dao.firebase.FirebaseUserDao;
import com.afeka.whereapp.data.Group;
import com.afeka.whereapp.data.Message;
import com.afeka.whereapp.data.User;
import com.afeka.whereapp.data.util.EntityFactory;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DataService {

    public static final String DATABASE_NAME = "whereapp";

    private UserDao userDao;
    private GroupDao groupDao;
    private EntityFactory entityFactory;
    private AppDatabase db;

    public DataService(Context context) {
        userDao = new FirebaseUserDao();
        entityFactory = new EntityFactory();
        groupDao = new FirebaseGroupDao();
        db = Room.databaseBuilder(context,
                AppDatabase.class, DATABASE_NAME).build();
    }

    public void getDatabaseUser(final String userId, final OnResponse<User> res) {
        userDao.getUserById(userId, new OnResponse<DataSnapshot>() {
            @Override
            public void onData(DataSnapshot data) {
                res.onData(entityFactory.createNewUser(userId, data.getValue(User.class)));
            }

            @Override
            public void onError(String msg) {
                res.onError(msg);
            }
        });
    }

    public void getGroups(final OnResponse<List<Group>> res) {
        groupDao.readAll(new OnResponse<DataSnapshot>() {
            @Override
            public void onData(DataSnapshot data) {
                ArrayList<Group> groupList = new ArrayList<Group>();
                for (DataSnapshot g : data.getChildren()) {
                    groupList.add(entityFactory.createNewGroup(g.getKey(), g.getValue(Group.class)));
                }
                res.onData(groupList);
            }

            @Override
            public void onError(String msg) {
                res.onError(msg);
            }
        });
    }

    public void getGroupById(final String id, final OnResponse<Group> res) {
        groupDao.readById(id, new OnResponse<DataSnapshot>() {
            @Override
            public void onData(DataSnapshot data) {
                Group group = entityFactory.createNewGroup(id, data.getValue(Group.class));
                if (group == null) {
                    onError("no such group");
                } else {
                    res.onData(group);
                }
            }

            @Override
            public void onError(String msg) {
                res.onError(msg);
            }
        });
    }

    public void registerNewUser(String id, String name, String email, OnResponse<User> res) {
        userDao.create(entityFactory.createNewUser(id, name, email, null, null), res);
    }

    public void saveMessage(Message msg) {
        db.messageDao().insertAll(msg);
    }

    public List<Message> getAllMessages() {
        return db.messageDao().getAll();
    }

    public List<Message> getAllMessagesByGroupId(String id) {
        return db.messageDao().loadAllByGroupId(id);
    }

    public void createGroup(String name, String description, Location location) {
        groupDao.create(entityFactory.createNewGroup(null, name,
                location.getLatitude(), location.getLongitude(), description));
    }
}
