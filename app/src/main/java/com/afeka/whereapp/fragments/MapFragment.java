package com.afeka.whereapp.fragments;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afeka.whereapp.MainActivity;
import com.afeka.whereapp.R;
import com.afeka.whereapp.data.Group;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private final float MAP_ZOOM = 16.0f;
    private final String JOIN_GROUP_BUTTON_TEXT = "Join Group";
    private final String LEAVE_GROUP_BUTTON_TEXT = "Leave Group";

    private GoogleMap mMap;
    private HashMap<String, Group> groupHashMap = new HashMap<>();
    private ArrayList<MarkerOptions> markersToAdd = new ArrayList<MarkerOptions>();
    private LatLng currentLatlng;
    private TextView groupName;
    private TextView groupDescription;
    private TextView groupDist;
    private Button joinGroupButton;

    private Location currentLocation;

    public MapFragment() {
    }


    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_map, container, false);

        groupName = v.findViewById(R.id.group_info_name);
        groupDescription = v.findViewById(R.id.group_info_description);
        groupDist = v.findViewById(R.id.group_info_dist);
        joinGroupButton = v.findViewById(R.id.join_group_button);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addGroupsToMap(new ArrayList<Group>(groupHashMap.values()));
        if (currentLatlng != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatlng, MAP_ZOOM));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final Group group = (Group) marker.getTag();
                groupName.setText(group.getName());
                groupDescription.setText(group.getDescription());

                if (currentLocation != null) {
                    Location groupLocation = new Location(LocationManager.GPS_PROVIDER);
                    groupLocation.setLatitude(group.getLocation().getLat());
                    groupLocation.setLongitude(group.getLocation().getLng());
                    groupDist.setText(String.format("%.2fm", currentLocation.distanceTo(groupLocation)));
                }


                joinGroupButton.setVisibility(View.VISIBLE);
                final boolean isInGroup = ((MainActivity)getActivity()).isUserInGroup(group.getId());
                if (isInGroup) {
                    joinGroupButton.setText(LEAVE_GROUP_BUTTON_TEXT);
                } else {
                    joinGroupButton.setText(JOIN_GROUP_BUTTON_TEXT);
                }
                joinGroupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isInGroup) {
                            ((MainActivity)getActivity()).registerToGroup(group.getId());
                            joinGroupButton.setText(LEAVE_GROUP_BUTTON_TEXT);
                        }
                    }
                });

                return false;
            }
        });
    }

    public void addGroupsToMap(List<Group> groups) {
        for (Group g : groups) {
            groupHashMap.put(g.getId(), g);
            addMarker(g);
        }
    }

    public void zoomToCurrentLocation(@NonNull Location location) {
        currentLocation = location;
        currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatlng, 5.0f));
        }
    }

    private void addMarker(Group group) {
        LatLng marker = new LatLng(group.getLocation().getLat(), group.getLocation().getLng());
        MarkerOptions markerOptions = new MarkerOptions().position(marker).title(group.getName());
        markersToAdd.add(markerOptions);
        if (mMap != null) {
            mMap.addMarker(markerOptions).setTag(group);
        }
    }
}
