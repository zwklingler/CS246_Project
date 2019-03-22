package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefs {
    private Context context;

    SharedPrefs(Context c) {
        this.context = c;
    }

    public Fences load() {
        //TODO actually load allZones into shared preferences
        //Load allZones using GSON from a file
        SharedPreferences pref = context.getSharedPreferences("Geofences", MODE_PRIVATE);
        String s = pref.getString("Fences", null);         // getting String

        Gson gson = new Gson();
        Fences fences = gson.fromJson(s, Fences.class);
        return fences;
    }

    public void save(Fences fences) {
        //TODO actually save allZones into shared preferences

        Gson gson = new Gson();
        final String s = gson.toJson(fences.getAllZones());

        //Save allZones using GSON to a file
        SharedPreferences pref = context.getSharedPreferences("Geofences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("Fences", s);

        editor.apply();
    }

    public String getPref() {
        SharedPreferences pref = context.getSharedPreferences("Geofences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String s = pref.getString("Fences", null);
        return s;
    }
}