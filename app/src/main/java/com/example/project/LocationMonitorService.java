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
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Uses a service for consistent and constant location updates from a location listener.
 */
public class LocationMonitorService extends Service {

    LocationManager mLocationManager = null;

    /**
     * Listens for location updates
     */
    private class LocationListen implements android.location.LocationListener {
        Location mLastLocation;

        /**
         * Sets location listener.
         * @param provider The provider being used.
         */
        public LocationListen(String provider) {
            Log.i("LocationMonitor: ", "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        /**
         * Performed when location is updated.
         * @param location The new location getting recieved.
         */
        @Override
        public void onLocationChanged(Location location) {
            Log.i("LocationMonitor: ", "onLocationChanged: " + location);
            mLastLocation.set(location);
        }

        /**
         * Performed when provider is disabled.
         * @param provider Which provider is being used.
         */
        @Override
        public void onProviderDisabled(String provider) {
            Log.i("LocationMonitor: ", "onProviderDisabled: " + provider);
        }

        /**
         * Performed when the provider is enabled.
         * @param provider Which provider is being used.
         */
        @Override
        public void onProviderEnabled(String provider) {
            Log.i("LocationMonitor: ", "onProviderEnabled: " + provider);
        }

        /**
         * Performed when the status changes.
         * @param provider Which provider is being used.
         * @param status What is the status.
         * @param extras Any extra bundle.
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("LocationMonitor: ", "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            //One for GPS and one for Network for more accuracy
            new LocationListen(LocationManager.GPS_PROVIDER),
            new LocationListen(LocationManager.NETWORK_PROVIDER)
    };


    /**
     * Performed when client first binds.
     * @param intent Intent for binding.
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Performed at the start of the service.
     * @param intent The intent calling the function.
     * @param flags Were there any flags.
     * @param startId What is the ID for starting.
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocationMonitor: ", "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    /**
     * Checks for permissions, and then it sets up a location manager for GPS and one for Network.
     */
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
        //Try to make GPS Location Updater
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 10000, 5,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.d("LocationMonitor", "Failed to get location update", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("LocationMonitor", "GPS Provider does not exist " + ex.getMessage());
        }

        //Try to make Network Location Updater
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

    /**
     * Removes location monitors upon destruction of the service.
     */
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

    /**
     * Initialize the location manager.
     */
    private void initializeLocationManager() {
        Log.i("LocationMonitor: ", "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}