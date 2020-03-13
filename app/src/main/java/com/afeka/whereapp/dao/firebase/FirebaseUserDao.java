package com.afeka.whereapp.dao.firebase;

import androidx.annotation.NonNull;

import com.afeka.whereapp.dao.OnResponse;
import com.afeka.whereapp.dao.UserDao;
import com.afeka.whereapp.data.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUserDao implements UserDao {

    private final String NODE_NAME = "users";
    private DatabaseReference dbRef;

    public FirebaseUserDao() {
        this.dbRef = FirebaseDatabase.getInstance().getReference().child(NODE_NAME);
    }

    @Override
    public User create(User user) {
        this.dbRef.child(user.getId()).setValue(user);
        return user;
    }

    @Override
    public void getUserById(String id, final OnResponse<DataSnapshot> res) {
        this.dbRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                res.onData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                res.onError(databaseError.getDetails());
            }
        });
    }
}
