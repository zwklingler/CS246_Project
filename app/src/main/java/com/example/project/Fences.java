package com.example.project;

import com.google.android.gms.location.LocationCallback;

import java.util.ArrayList;
import java.util.List;

public class Fences extends LocationCallback {

    private List<Zone> allZones;

    Fences() {
        allZones = new ArrayList<>();
    }
    public void deleteZone(String id) {
        //Search through allZones and remove zone with ID
    }

    public void addZone(Zone zone) {
        allZones.add(zone);
    }

    public List<Zone> getAllZones() {
        return allZones;
    }

    public void save() {
        //Save allZones using GSON to a file
    }

    public void load() {
        //Load allZones using GSON from a file
    }

    public void addGeofences() {

    }
}
