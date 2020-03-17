package com.afeka.whereapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.afeka.whereapp.dao.MessageDao;
import com.afeka.whereapp.data.Message;

@Database(entities = {Message.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MessageDao messageDao();
}
