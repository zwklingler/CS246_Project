package com.example.project;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat;
    private double lon;

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Update when user drags marker
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d("Location: " ,"latitude : "+ marker.getPosition().latitude);
                lat = marker.getPosition().latitude;
                lon = marker.getPosition().longitude;

                marker.setSnippet(String.valueOf(marker.getPosition().latitude));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });
        lat = -31.9;
        lon = 115.86;
        final LatLng PERTH = new LatLng(lat, lon);
        MarkerOptions options = new MarkerOptions()
                .position(PERTH)
                .draggable(true)
                .title("Location");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PERTH));
    }

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
            if (radius < 500) {
                Toast.makeText(this, "Radius must be at least 500",
                        Toast.LENGTH_LONG).show();
                canSend = false;
            }
        }

        if (canSend == true) {
            Toast.makeText(this, "Latitude: " + lat + "     Longitude: " + lon,
                    Toast.LENGTH_LONG).show();

            ChangeRinger cr = new ChangeRinger(this);
            cr.changeRinger();
        }



    }
}
