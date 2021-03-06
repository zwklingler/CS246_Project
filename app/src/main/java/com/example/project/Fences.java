package com.example.project;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

/**
 * Creates geofences from a List of Zones.
 */
public class Fences {

    @SerializedName("allZones")
    private List<Zone> allZones;

    private transient Context context;
    private transient List<Geofence> allFences;

    /**
     * Initializes All Zones and All Fences and saves the context.
     * @param context Context to be saved and used for creating Geofences.
     */
    Fences(Context context) {
        allZones = new ArrayList<>();
        allFences = new ArrayList<>();
        this.context = context;
    }

    /**
     * Initializes All Zones and All Fences.
     */
    Fences() {
        allZones = new ArrayList<>();
        allFences = new ArrayList<>();
    }

    /**
     * Deletes a zone from geofences, and from shared preferences.
     * @param id Name of the geofence to be deleted.
     */
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

        List<String> idList = new ArrayList<>();
        idList.add(id);
        GeofencingClient geofencingClient = new GeofencingClient(context);
        //Checks if there is there is a permission for ACCESS_FINE_LOCATION
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //They should have permissions, but if for some reason they don't, an error will be logged
            Log.e("Fences: ", "Location Permission Not Granted When Trying to Remove Geofence");
            return;
        }
        Log.i("Fences: ", "Going to Try Removing Geofence with id: " + id);
        geofencingClient.removeGeofences(idList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("Fences: ", "Geofence removed successfully");
                // Geofences removed
                // ...
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Fences: ", "Failed to Remove Geofence");
                        Log.e("Fences: ", e.getMessage());
                        // Failed to remove geofences
                        // ...
                    }
                });
    }

    /**
     * Adds a zone to All Zones.
     * @param zone Will be added to all zones.
     */
    public void addZone(Zone zone) {
        Log.d("Adding Zone: ",zone.getName());
        //Add zone to allZones
        allZones.add(zone);
    }


    public List<Zone> getAllZones() {
        return allZones;
    }

    /**
     * Creates geofence objects from each Zone in All Zones, and store them in All Fences. All Fences gets added to the geofence builder.
     * Intent is created for entrance and exit in geofence.
     */
    public void addGeofences() {
        Log.d("Geofencing: ","Creating Geofences from Zones");
        //Add only the newest zone because the rest are already in place
        Zone zone  = allZones.get(allZones.size() - 1);
        allFences.add(createGeofence(zone.getLatitude(), zone.getLongitude(), zone.getRadius(), zone.getName()));

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
            //They should have permissions, but if for some reason they don't, an error will be logged
            Log.e("Fences: ", "Location Permission Not Granted");
            return;
        }
        Log.i("Fences: ", "Going to try adding Intent for Change Ringer");

        geofencingClient.addGeofences(builder.build(), geofencePendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
            /**
             * Logs correctly added geofences
             * @param aVoid
             */
            @Override
            public void onSuccess(Void aVoid) {
                //Geofences Added
                Log.i("Geofences", "Correctly Added Geofences");

            }
        }).addOnFailureListener(new OnFailureListener(){
            /**
             * Logs error on failure to add geofences.
             * @param e
             */
            @Override
            public void onFailure(@NonNull Exception e) {
                //Failed to add Geofences
                String errorMessage = "Failed to Add Geofences";
                Log.e("Geofences", errorMessage);
                Log.e("Geofenes", e.getMessage());
            }
        });
    }

    //This is called in addGeofences()

    /**
     * Creates a geofence object that would sent an intent on entrance and exit.
     * @param latitude Latitude of the geofence center.
     * @param longitude Longitude of the geofence center.
     * @param radius Radius of the geofence.
     * @param name Name of the geofence.
     * @return Returns the created geofence.
     */
    public Geofence createGeofence(double latitude, double longitude, int radius, String name) {
        Geofence geofence = new Geofence.Builder().setRequestId(name) // Geofence ID
                .setCircularRegion( latitude, longitude, radius) // defining fence region
                // Transition types that it should look for
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        Log.i("Fences: ", "Adding geofence named " + name);
        return geofence;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

