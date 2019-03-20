package com.example.project;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.media.AudioManager.STREAM_RING;

/**
 * Generates a google map and input for creating geofences.
 */
public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat;
    private double lon;

    /**
     * Creates map fragment when activity is started
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    /**
     * Creates google map and places marker at current location.
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Update when user drags marker
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            /**
             * Performed at the start of dragging marker
             * @param marker
             */
            @Override
            public void onMarkerDragStart(Marker marker) {
                //Nothing
            }

            /**
             * Moves marker after it has been dragged
             * @param marker
             */
            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d("Location: ", "latitude : " + marker.getPosition().latitude);
                lat = marker.getPosition().latitude;
                lon = marker.getPosition().longitude;

                marker.setSnippet(String.valueOf(marker.getPosition().latitude));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

            }

            /**
             * Performed while dragging marker
             * @param marker
             */
            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });

        //Check if location is allowed, which it should
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Maps", "No permissions");
            return;
        }

        //This gets the current Location and if it's null for some reason it sets Lat and Lon to default values
        LocationManager locationManager = (LocationManager)this.getSystemService(this.LOCATION_SERVICE);
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocation == null) {
            Log.e("Maps", "Not Available");
            lat = 37;
            lon = -120;
        }
        else {
            lat = lastLocation.getLatitude();
            lon = lastLocation.getLongitude();
        }


        final LatLng location = new LatLng(lat, lon);
        MarkerOptions options = new MarkerOptions()
                .position(location)
                .draggable(true)
                .title("Location");
        mMap.addMarker(options);
        float zoom = 18;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
    }

    /**
     * Validates input, saves geofence, and starts intent to Main Activity.
     * @param view
     */
    public void send(View view) {
        EditText eRadius = findViewById(R.id.editText2);
        EditText eName = findViewById(R.id.editText3);

        String name = eName.getText().toString().trim();
        String sRadius = eRadius.getText().toString().trim();
        boolean canSend = true;
        int radius = 0;

        if (name.isEmpty() || name.length() == 0 || name.equals("") || name == null) {
            Toast.makeText(this, "Enter a name",
                    Toast.LENGTH_LONG).show();
            canSend = false;
        }
        else if (sRadius.isEmpty() || sRadius.length() == 0 || sRadius.equals("") || sRadius == null) {
            Toast.makeText(this, "Enter a radius",
                    Toast.LENGTH_LONG).show();
            canSend = false;
        }
        else {
            radius = Integer.parseInt(sRadius);
            if (radius < 100 || radius > 10000) {
                Toast.makeText(this, "Radius must be between 100 and 10000",
                        Toast.LENGTH_LONG).show();
                canSend = false;
            }
        }

        if (canSend == true) {
            //Toast.makeText(this, "Latitude: " + lat + "     Longitude: " + lon,
              //      Toast.LENGTH_LONG).show();

            Zone z = new Zone();
            z.setLatitude(lat);
            z.setLongitude(lon);
            z.setName(name);
            z.setRadius(radius);
            //TODO create fences object and perform necessary functions


            SharedPrefs sp = new SharedPrefs(this);
            Fences f = new Fences(this);
            f.addZone(z);

            //Creates geofences from zones
            f.addGeofences();

            Log.i("Maps", "Made it passed f.addZone(z)");

            //TODO FIX this STUPID LINE OF CODE THAT BREAKS EVERYTHING
            /*sp.save(f);

            String s = sp.getPref();
            Toast.makeText(this, s,
                    Toast.LENGTH_LONG).show();
            */

            Log.i("Intent Debug: ","Starting Main Activity");
            //Create Intent
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("isSaved", true);
            startActivity(intent);
        }
    }

}
