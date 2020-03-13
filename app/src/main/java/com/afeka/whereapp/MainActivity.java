package com.afeka.whereapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afeka.whereapp.dao.OnResponse;
import com.afeka.whereapp.dao.firebase.FirebaseUserDao;
import com.afeka.whereapp.data.util.EntityFactory;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private final String USER_ID_EXTRA = "user_id";
    private final String LOCATION_EXTRA = "location";

    private FragmentManager fragmentManager;
    private Location currentLocation;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        currentLocation = new Location(LocationManager.GPS_PROVIDER);
        currentLocation.setLatitude(23.5678);
        currentLocation.setLongitude(34.456);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final MapFragment fragment = new MapFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        LocationService locationService = new LocationService(this);
        locationService.getCurrentLocation(this, new OnLocationResult() {
            @Override
            public void onLocationFound(Location location) {
                currentLocation = new Location(location);
            }

            @Override
            public void onError(String msg) {
                return;
            }
        });

        findViewById(R.id.add_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddGroupActivity.class);
                intent.putExtra(LOCATION_EXTRA, currentLocation);
                //intent.putExtra(USER_ID_EXTRA, currentUser.getUid());
                startActivity(intent);
            }
        });

        findViewById(R.id.map_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new MapFragment());
            }
        });

        findViewById(R.id.list_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new ListFragment());
            }
        });

        findViewById(R.id.chat_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new ChatFragment());
            }
        });
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

    public void showFragment(Fragment f) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, f);
        fragmentTransaction.commit();
    }
}
