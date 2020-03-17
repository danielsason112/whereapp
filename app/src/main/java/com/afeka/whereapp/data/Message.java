package com.afeka.whereapp.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {
    @NonNull
    @PrimaryKey
    private String id;
    @ColumnInfo(name = "text")
    private String text;
    @ColumnInfo(name = "sender")
    private String sender;
    @ColumnInfo(name = "group_id")
    private String groupId;
    @ColumnInfo(name = "timestamp")
    private long timestamp;

    public Message(String id, String text, String sender, String groupId, long timestamp) {
        this.id = id;
        this.text = text;
        this.sender = sender;
        this.groupId = groupId;
        this.timestamp = timestamp;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
