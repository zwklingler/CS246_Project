package com.example.project;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Fences {

    @SerializedName("allZones")
    private List<Zone> allZones;

    private List<Geofence> allFences;

    Fences() {
        allZones = new ArrayList<>();
    }

    public void deleteZone(String id) {
        //Search through allZones and remove zone with ID
        //TODO delete zone with id name from saved preferences
    }

    public void addZone(Zone zone, Context context) {
        //Load saved zones
        load(context);
        //Add zone to allZones
        allZones.add(zone);
        //Save allZones with the new zone added
        save(context);
    }

    public List<Zone> getAllZones() {
        return allZones;
    }

    public void save(Context context) {
        //TODO actually save allZones into shared preferences
        //Save allZones using GSON to a file
        final String s = "Some GSON Stuff";
        SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("GSON", s);
        editor.apply();

    }

    public void load(Context context) {
        //TODO actually load allZones into shared preferences
        //Load allZones using GSON from a file
        SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
        String s = pref.getString("GSON", null);         // getting String


    }

    public void addGeofences(Context context) {
        //Loads all of the Zone variables
        load(context);

        //Create a geofence for each zone
        for (Zone zone : allZones) {
            //TODO check if geofence is already implace, and if so don't add it to the list
            allFences.add(createGeofence(zone.getLatitude(), zone.getLongitude(), zone.getRadius(), zone.getName()));
        }

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        //Check for exit and enter
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(allFences);

        //Create Intent to be called when the Geofence is entered/exited
        Intent intent = new Intent(context, ChangeRinger.class);
        PendingIntent geofencePendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        GeofencingClient geofencingClient = new GeofencingClient(context);
        //Checks if there is there is a permission for ACCESS_FINE_LOCATION
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling permissions
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(builder.build(), geofencePendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Geofences Added

            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e) {
                //Failed to add Geofences
                String errorMessage = "Failed to Add Geofences";
                Log.e("Geofences", errorMessage);
            }
        });


    }

    public Geofence createGeofence(double latitude, double longitude, int radius, String name) {
        Geofence geofence = new Geofence.Builder().setRequestId(name) // Geofence ID
                .setCircularRegion( latitude, longitude, radius) // defining fence region
                // Transition types that it should look for
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
        return geofence;
    }

}

