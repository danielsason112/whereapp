package com.afeka.whereapp.dao.firebase;

import androidx.annotation.NonNull;

import com.afeka.whereapp.dao.OnResponse;
import com.afeka.whereapp.dao.UserDao;
import com.afeka.whereapp.data.User;
import com.afeka.whereapp.data.util.EntityFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUserDao implements UserDao {

    private static final String TOKEN_NODE = "token";
    private final String NODE_NAME = "users";
    private final String GROUPS_NODE = "groups";
    private DatabaseReference dbRef;

    public FirebaseUserDao() {
        this.dbRef = FirebaseDatabase.getInstance().getReference().child(NODE_NAME);
    }

    @Override
    public User create(final User user, final OnResponse<User> res) {
        this.dbRef.child(user.getId()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                res.onData(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                res.onError(e.getMessage());
            }
        });
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

    @Override
    public void getTokenById(String userId, final OnResponse<DataSnapshot> res) {
        this.dbRef.child(userId).child(TOKEN_NODE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void updateTokenById(String userId, String token) {
        this.dbRef.child(userId).child(TOKEN_NODE).setValue(token);
    }

    @Override
    public void addGroupById(String userId, String groupId) {
        this.dbRef.child(userId).child(GROUPS_NODE).child(groupId).setValue(true);
    }
}
