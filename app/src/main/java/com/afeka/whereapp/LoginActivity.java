package com.afeka.whereapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.afeka.whereapp.dao.OnResponse;
import com.afeka.whereapp.dao.firebase.FirebaseUserDao;
import com.afeka.whereapp.data.User;
import com.afeka.whereapp.data.util.EntityFactory;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // User is logged in
        if (user != null) {
            final FirebaseUserDao firebaseUserDao = new FirebaseUserDao();

            firebaseUserDao.getUserById(user.getUid(), new OnResponse<DataSnapshot>() {
                @Override
                public void onData(DataSnapshot data) {
                    Log.d("login", data.toString());
                    // User doesn't exists in the db
                    if (data.getValue() == null) {
                        EntityFactory entityFactory = new EntityFactory();
                        firebaseUserDao.create(entityFactory.createNewUser(user.getUid(), user.getDisplayName(), user.getEmail()));
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.e("login", msg);
                }
            });
        } else {
            startAuthActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startAuthActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startAuthActivity() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
}
