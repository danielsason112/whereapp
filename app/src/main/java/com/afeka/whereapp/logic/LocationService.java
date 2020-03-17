package com.afeka.whereapp.logic;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationService {

    private FusedLocationProviderClient fusedLocationClient;

    public LocationService(Activity context) {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void getCurrentLocation(Activity activity, final OnLocationResult callback) {
        this.fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    callback.onLocationFound(location);
                    return;
                }
                callback.onError("Current location could not been found.");
            }
        });
    }
}

