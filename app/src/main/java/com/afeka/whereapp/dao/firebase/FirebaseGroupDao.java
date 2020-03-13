package com.afeka.whereapp.dao.firebase;


import androidx.annotation.NonNull;
import com.afeka.whereapp.dao.GroupDao;
import com.afeka.whereapp.dao.OnResponse;
import com.afeka.whereapp.data.Group;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FirebaseGroupDao implements GroupDao {

    private final String NODE_NAME = "groups";
    private DatabaseReference dbRef;

    public FirebaseGroupDao() {
        this.dbRef = FirebaseDatabase.getInstance().getReference().child(NODE_NAME);
    }

    @Override
    public Group create(Group group) {
        this.dbRef.child(group.getId()).setValue(group);
        return group;
    }

    @Override
    public void readAll(final OnResponse res) {
        this.dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    public void delete(Group group) {

    }

    @Override
    public void deleteAll() {

    }
}
