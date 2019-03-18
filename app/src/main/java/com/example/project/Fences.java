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

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import static android.content.Context.MODE_PRIVATE;

public class Fences {

    @SerializedName("allZones")
    private List<Zone> allZones;

    private Context context;
    private List<Geofence> allFences;

    Fences(Context context) {
        allZones = new ArrayList<>();
        this.context = context;
    }

    Fences() {
        allZones = new ArrayList<>();
    }

    public void deleteZone(String id) {
        //Search through allZones and remove zone with ID
        Log.d("Deleting Zone: ",id);
        Zone zone = null;
        for (Zone z : allZones) {
            if (z.getName() == id) {
                zone = z;
            }
        }
        if (zone != null) {
            allZones.remove(zone);
        }
        //TODO delete zone with id name from active geofences

    }

    public void addZone(Zone zone) {
        Log.d("Adding Zone: ",zone.getName());
        //Add zone to allZones
        allZones.add(zone);
    }

    public List<Zone> getAllZones() {
        return allZones;
    }

    public void addGeofences() {
        Log.d("Geofencing: ","Creating Geofences from Zones");
        //Create a geofence for each zone
        for (Zone zone : allZones) {
            //TODO check if geofence is already in place, and if so don't add it to the list
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
                Log.i("Geofences", "Correctly Added Geofence");

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

    //This is called in addGeofences()
    public Geofence createGeofence(double latitude, double longitude, int radius, String name) {
        Geofence geofence = new Geofence.Builder().setRequestId(name) // Geofence ID
                .setCircularRegion( latitude, longitude, radius) // defining fence region
                // Transition types that it should look for
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
        return geofence;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

