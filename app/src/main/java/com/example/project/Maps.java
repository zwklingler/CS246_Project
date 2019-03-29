package com.example.project;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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
    private Circle circle;
    private double lat;
    private double lon;

    /**
     * Creates map fragment when activity is started. Updates circle on the map after the
     * Edit Text for radius is changed.
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

        //Checks for when radius input is changed
        EditText radiusInput = (EditText) findViewById(R.id.editText2);
        radiusInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCircle();
            }
        });
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
     * @param googleMap Google Map to be displayed to the user.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Update when user drags marker
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            /**
             * Updates the circle when the marker starts to get dragged.
             * @param marker Marker that is starting to get dragged.
             */
            @Override
            public void onMarkerDragStart(Marker marker) {
                lat = marker.getPosition().latitude;
                lon = marker.getPosition().longitude;
                updateCircle();
            }

            /**
             * Moves marker after it has been dragged.
             * @param marker Marker that has been dragged.
             */
            @Override
            public void onMarkerDragEnd(Marker marker) {
                //Updates latitude and longitude after marker has been dropped
                Log.d("Location: ", "latitude : " + marker.getPosition().latitude);
                lat = marker.getPosition().latitude;
                lon = marker.getPosition().longitude;

                marker.setSnippet(String.valueOf(marker.getPosition().latitude));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                updateCircle();

            }

            /**
             * Moves the circle object while dragging the marker.
             * @param marker Marker getting dragged.
             */
            @Override
            public void onMarkerDrag(Marker marker) {
                lat = marker.getPosition().latitude;
                lon = marker.getPosition().longitude;
                updateCircle();
            }

        });

        //Check if location is allowed, which it should
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Maps", "No permissions");
            return;
        }

        //This gets the current Location and if it's null for some reason it sets Lat and Lon to default values
        LocationManager locationManager = (LocationManager)this.getSystemService(this.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            /**
             * Performed when the user's location changes.
             * @param location The user's new location.
             */
            @Override
            public void onLocationChanged(Location location) {
                //Callback for when location is updated
                Log.i("Maps: ", "Location Listener Callback was Called");
                Log.i("Maps: ", "Latitude: " + location.getLatitude() + "   Longitude: " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Callback when the status is updated
            }

            /**
             * Logs the specified provider as enabled.
             * @param provider The provider updating the user's location.
             */
            @Override
            public void onProviderEnabled(String provider) {
                //Callback when Provider Permission is Enabled
                Log.i("Maps: ", "Provider is Enabled: " + provider);

            }

            /**
             * Logs the specified provider as disabled.
             * @param provider The provider updating the user's location.
             */
            @Override
            public void onProviderDisabled(String provider) {
                //Callback when Provider Permission is Disabled
                Log.i("Maps: ", "Provider is Disabled: " + provider);


            }

        };
        long minTime = 5 * 1000; // Minimum time interval for update in seconds, i.e. 5 seconds.
        long minDistance = 10; // Minimum distance change for update in meters, i.e. 10 meters.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocation == null) {
            Log.e("Maps", "Not Available");
            lat = 37;
            lon = -120;
        }
        else {
            lat = lastLocation.getLatitude();
            lon = lastLocation.getLongitude();
            Log.i("Maps: ", "Latitude: " + lat + "   " + "Longitude" + lon);
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
     * Updates the circle on google maps based on Lat and Lon variables.
     */
    public void updateCircle() {
        EditText radiusInput = (EditText) findViewById(R.id.editText2);
        String radiusString = radiusInput.getText().toString();
        if (mMap != null && (TextUtils.isEmpty(radiusString) != true)) {
            int rad = Integer.parseInt(radiusString);

            if (rad >= 100 && rad <= 10000) {
                //Current Color is Transparent
                int color = 0x95e9cc;
                if (circle == null) {
                    CircleOptions circleOptions = new CircleOptions();
                    LatLng latLng = new LatLng(lat, lon);
                    circleOptions.center(latLng);
                    circleOptions.radius(rad);

                    circleOptions.fillColor(color);
                    circle = mMap.addCircle(circleOptions);
                }
                else {
                    LatLng latLng = new LatLng(lat, lon);
                    circle.setCenter(latLng);
                    circle.setRadius(rad);
                    circle.setFillColor(color);
                }
            }
            else if (circle != null){
                circle.remove();
                circle = null;
            }
        }
    }

    /**
     * Validates input, saves geofence, and starts intent to Main Activity.
     * @param view View required for having an on click function for a button.
     */
    public void send(View view) {
        //Gets radius and name
        EditText eRadius = findViewById(R.id.editText2);
        EditText eName = findViewById(R.id.editText3);

        //Converts radius to int and name to string
        String name = eName.getText().toString().trim();
        String sRadius = eRadius.getText().toString().trim();
        boolean canSend = true;
        int radius = 0;

        //Validates input
        if (name.isEmpty() || name.length() == 0 || name.equals("") || name == null) {
            Toast.makeText(this, "Enter a name",
                    Toast.LENGTH_SHORT).show();
            canSend = false;
        }
        else if (sRadius.isEmpty() || sRadius.length() == 0 || sRadius.equals("") || sRadius == null) {
            Toast.makeText(this, "Enter a radius",
                    Toast.LENGTH_SHORT).show();
            canSend = false;
        }
        else {
            radius = Integer.parseInt(sRadius);
            if (radius < 100 || radius > 10000) {
                Toast.makeText(this, "Radius must be between 100 and 10000",
                        Toast.LENGTH_SHORT).show();
                canSend = false;
            }
        }

        //If there were not any errors
        if (canSend == true) {
            Zone zone = new Zone();
            zone.setLatitude(lat);
            zone.setLongitude(lon);
            zone.setName(name);
            zone.setRadius(radius);

            //Create thread for performing fences functions
            FencesThread fencesThread = new FencesThread(this, this, zone);
            Thread t = new Thread(fencesThread);
            t.start();

            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.i("Intent Debug: ","Starting Main Activity");
            //Create Intent
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

}
