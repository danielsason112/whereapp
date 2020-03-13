package com.afeka.whereapp;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

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

