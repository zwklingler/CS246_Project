package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * A thread to perform fences functions to create geofences.
 */
public class FencesThread implements Runnable {
    private WeakReference<Activity> activity;
    private Context context;
    private Zone zone;

    /**
     *
     * @param a Activity for a weak reference.
     * @param c Context needed for creating geofences.
     * @param z The Zone to be added to fences.
     */
    FencesThread(Activity a, Context c, Zone z) {
        activity = new WeakReference<>(a);
        context = c;
        zone = z;
    }

    /**
     * Loads zone list into fences object, adds the zone, creates the fences, and saves the fences object.
     */
    @Override
    public void run() {
        //Load fences object from SharedPrefs
        SharedPrefs sp = new SharedPrefs(context);

        Fences f = sp.load();
        if (f == null) {
            f = new Fences();
        }
        //Add zone to fences object
        f.setContext(context);
        f.addZone(zone);
        f.addGeofences();
        sp.save(f);

        Log.i("Maps", "Made it passed f.addZone(z)");

        //Return
        if (activity != null) {
            final Activity act = activity.get();
            act.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(act, "Quiet Zone Saved", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
