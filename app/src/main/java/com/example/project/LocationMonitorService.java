package com.example.project;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationMonitorService extends Service {

    LocationManager mLocationManager = null;

    private class LocationListen implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListen(String provider) {
            Log.i("LocationMonitor: ", "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i("LocationMonitor: ", "onLocationChanged: " + location);
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("LocationMonitor: ", "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("LocationMonitor: ", "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("LocationMonitor: ", "onStatusChanged: " + provider);
        }
    }

    LocationListen mLocationListen = new LocationListen(LocationManager.GPS_PROVIDER);
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListen(LocationManager.GPS_PROVIDER),
            new LocationListen(LocationManager.NETWORK_PROVIDER)
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocationMonitor: ", "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.i("LocationMonitor: ", "onCreate");
        initializeLocationManager();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("LocationMonitor: ", "Permission Denied");
            return;
        }

        Log.i("LocationMonitor", "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 10000, 5,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.d("LocationMonitor", "Failed to get location update", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("LocationMonitor", "GPS Provider does not exist " + ex.getMessage());
        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 10000, 5,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.d("LocationMonitor", "Failed to get location update", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("LocationMonitor", "Network Provider does not exist, " + ex.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        Log.i("LocationMonitor: ", "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i("LocationMonitor", "Failed to remove location listeners", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.i("LocationMonitor: ", "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}