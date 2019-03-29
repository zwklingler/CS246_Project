package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import static android.content.Context.MODE_PRIVATE;

public class SharedPrefs {
    private Context context;

    SharedPrefs(Context c) {
        this.context = c;
    }

    public Fences load() {
        //Load allZones using GSON from a file
        SharedPreferences pref = context.getSharedPreferences("Geofences", MODE_PRIVATE);
        String s = pref.getString("Fences", null);         // getting String

        Gson gson = new Gson();
        Fences fences = gson.fromJson(s, Fences.class);
        return fences;
    }

    public void save(Fences fences) {
        Gson gson = new Gson();

        final String s = gson.toJson(fences);

        Log.d("SharedPrefs: ", "GSON = " + s);

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
