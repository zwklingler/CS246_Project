package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import java.lang.Object;

public class Zone extends AppCompatActivity {
    private double latitude;
    private double longitude;
    private int radius;
    private String name;
    private Geofence geofence;


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getRadius() {
        return radius;
    }

    public String getName() {
        return name;
    }

    public void createGeofence() {
       Geofence geofence = new Geofence.Builder().setRequestId(name) // Geofence ID
               .setCircularRegion( latitude, longitude, radius) // defining fence region
               // Transition types that it should look for
               .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT )
               .build();
       this.geofence = geofence;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Geofence getGeofence() {
        return geofence;
    }
}
