package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static android.content.Context.MODE_PRIVATE;

/**
 * Uses GSON to convert to and from a string and it saves or loads those
 * strings with shared preferences.
 */
public class SharedPrefs {
    private Context context;

    /**
     * Stores context to be used for shared preferences.
     * @param c Context needed for saving to shared preferences.
     */
    SharedPrefs(Context c) {
        this.context = c;
    }

    /**
     * Loads fences string from shared preferences. Converts string into
     * Fences object.
     * @return Fences object from the string in shared preferences.
     */
    public Fences load() {
        //Load allZones using GSON from a file
        SharedPreferences pref = context.getSharedPreferences("Geofences", MODE_PRIVATE);
        String s = pref.getString("Fences", null);         // getting String

        Gson gson = new Gson();
        Fences fences = gson.fromJson(s, Fences.class);
        return fences;
    }

    /**
     * Converts fences object to a string using GSON.
     * Saves string into shared preferences.
     * @param fences Fences object that is going to be converted and saved
     *               in shared preferences.
     */
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

}
