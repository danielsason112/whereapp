package com.afeka.whereapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.afeka.whereapp.data.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM message")
    List<Message> getAll();

    @Query("SELECT * FROM message WHERE group_id LIKE :groupId ORDER BY timestamp ASC")
    List<Message> loadAllByGroupId(String groupId);

    @Insert
    void insertAll(Message... messages);
}
