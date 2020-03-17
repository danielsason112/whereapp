package com.afeka.whereapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.afeka.whereapp.adapters.TabAdapter;
import com.afeka.whereapp.dao.OnResponse;
import com.afeka.whereapp.data.Group;
import com.afeka.whereapp.data.User;
import com.afeka.whereapp.fragments.ChatFragment;
import com.afeka.whereapp.fragments.ListFragment;
import com.afeka.whereapp.fragments.MapFragment;
import com.afeka.whereapp.logic.AuthService;
import com.afeka.whereapp.logic.DataService;
import com.afeka.whereapp.logic.LocationService;
import com.afeka.whereapp.logic.MessagingService;
import com.afeka.whereapp.logic.OnLocationResult;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LOCATION_EXTRA = "location";

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 23;

    private final String MAP_FRAGMENT_TITLE = "Map";
    private final String LIST_FRAGMENT_TITLE = "List";
    private final String CHAT_FRAGMENT_TITLE = "Chats";
    private final double DEFAULT_LOCATION_LAT = 32.115115;
    private final double DEFAULT_LOCATION_LNG = 34.817933;

    private LocationService locationService;
    private MessagingService messagingService;
    private AuthService authService;
    private DataService dataService;

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Location currentLocation;
    private User currentUser;

    private MapFragment mapFragment;
    private ListFragment listFragment;
    private ChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messagingService = new MessagingService();
        authService = new AuthService();
        dataService = new DataService(this);
        locationService = new LocationService(this);

        mapFragment = new MapFragment();
        listFragment = new ListFragment();
        chatFragment = new ChatFragment();

        currentLocation = new Location(LocationManager.GPS_PROVIDER);
        currentLocation.setLatitude(DEFAULT_LOCATION_LAT);
        currentLocation.setLongitude(DEFAULT_LOCATION_LNG);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(mapFragment, MAP_FRAGMENT_TITLE);
        adapter.addFragment(listFragment, LIST_FRAGMENT_TITLE);
        adapter.addFragment(chatFragment, CHAT_FRAGMENT_TITLE);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkForPermissions();
        updateCurrentLocation();

        final FirebaseUser user = authService.getFirebaseCurrentUser();
        final Context context = this;
        // User is signed in
        if (user != null) {
            dataService.getDatabaseUser(user.getUid(), new OnResponse<User>() {
                @Override
                public void onData(final User dbUser) {
                    // User not created in db
                    if (dbUser == null) {
                        registerUser(user);
                    } else {
                        currentUser = dbUser;
                        if (currentUser.getGroups() != null) {
                            subscribeToUserGroups();
                        }
                    }
                    // Get Firebase Cloud Messaging token
                    messagingService.getToken(user.getUid());

                    chatFragment.updateUserName(user.getDisplayName());

                    loadGroups();
                }

                @Override
                public void onError(String msg) {
                    Log.e(TAG, msg);
                }
            });
        } else {
            Toast.makeText(this, "Please sign in or sign up", Toast.LENGTH_LONG).show();
            startActivityForResult(authService.getSignInIntent(), RC_SIGN_IN);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                authService.signOut();
                startActivityForResult(authService.getSignInIntent(), RC_SIGN_IN);
                return true;
            case R.id.new_group:
                Intent intent = new Intent(this, AddGroupActivity.class);
                intent.putExtra(LOCATION_EXTRA, currentLocation);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateCurrentLocation();
                } else {
                    // No Location permissions, set current location to a default value
                    currentLocation = new Location(LocationManager.GPS_PROVIDER);
                    currentLocation.setLatitude(DEFAULT_LOCATION_LAT);
                    currentLocation.setLongitude(DEFAULT_LOCATION_LNG);
                }
                return;
            }
        }
    }

    public void registerToGroup(String groupId) {
        if (currentUser != null) {
            messagingService.subscribeToGroup(currentUser.getId(), groupId);
            currentUser.addGroup(groupId);
        }
    }

    public boolean isUserInGroup(String groupId) {
        if (currentUser.getGroups() == null) {
            return false;
        }
        return currentUser.getGroups().containsKey(groupId);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    private void checkForPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    private void updateCurrentLocation() {
        locationService.getCurrentLocation(this, new OnLocationResult() {
            @Override
            public void onLocationFound(Location location) {
                if (location != null) {
                    currentLocation = new Location(location);
                }
                mapFragment.zoomToCurrentLocation(currentLocation);
            }

            @Override
            public void onError(String msg) {
                return;
            }
        });
    }

    private void registerUser(final FirebaseUser user) {
        dataService.registerNewUser(user.getUid(), user.getDisplayName(), user.getEmail(), new OnResponse<User>() {
            @Override
            public void onData(User data) {
                currentUser = data;
                Log.d(TAG, "Created a new user in db: " + user.toString());
                if (currentUser.getGroups() != null) {
                    subscribeToUserGroups();
                }
            }

            @Override
            public void onError(String msg) {
                Log.e(TAG, msg);
            }
        });
    }

    private void subscribeToUserGroups() {
        final Context context = this;
        for (String key : currentUser.getGroups().keySet()) {
            messagingService.subscribeToGroup(currentUser.getId(), key);
            dataService.getGroupById(key, new OnResponse<Group>() {
                @Override
                public void onData(Group data) {
                    chatFragment.addGroup(context, data);
                }

                @Override
                public void onError(String msg) {
                    Log.e(TAG, msg);
                }
            });
        }
    }

    private void loadGroups() {
        dataService.getGroups(new OnResponse<List<Group>>() {
            @Override
            public void onData(List<Group> data) {
                mapFragment.addGroupsToMap(data);
                listFragment.addGroupsToList(data);
            }

            @Override
            public void onError(String msg) {
                Log.e(TAG, msg);
            }
        });
    }

    public void addGroupToChatList(Group group) {
        chatFragment.addGroup(this, group);
    }
}