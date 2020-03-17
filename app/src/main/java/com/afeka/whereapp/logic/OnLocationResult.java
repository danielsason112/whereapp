package com.afeka.whereapp.logic;

import android.location.Location;

public interface OnLocationResult {
    void onLocationFound(Location location);
    void onError(String msg);
}
