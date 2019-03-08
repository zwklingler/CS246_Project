package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class Fences {

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

    public void save(Context context) {
        //Save allZones using GSON to a file
        final String s = "Some GSON Stuff";
        SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("GSON", s);
        editor.apply();

    }

    public void load(Context context) {
        //Load allZones using GSON from a file
        SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
        String s = pref.getString("GSON", null);         // getting String


    }

    public void addGeofences() {

    }
}
