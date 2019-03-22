package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class FencesThread implements Runnable {
    private WeakReference<Activity> activity;
    private Context context;
    private Zone zone;

    FencesThread(Activity a, Context c, Zone z) {
        activity = new WeakReference<>(a);
        context = c;
        zone = z;
    }

    @Override
    public void run() {
        //Do the fences stuff
        //TODO FIX this STUPID LINE OF CODE THAT BREAKS EVERYTHING
        /*sp.save(f);

          String s = sp.getPref();
          Toast.makeText(this, s,
                    Toast.LENGTH_LONG).show();
            */

        //TODO create fences object and perform necessary functions
        SharedPrefs sp = new SharedPrefs(context);
        Fences f = new Fences(context);
        f.addZone(zone);


        //Creates geofences from zones
        f.addGeofences();

        Log.i("Maps", "Made it passed f.addZone(z)");



        //Return
        if (activity != null) {
            final Activity act = activity.get();
            act.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(act, "Quiet Zone Saved", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
