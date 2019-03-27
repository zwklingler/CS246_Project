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
        SharedPrefs sp = new SharedPrefs(context);

        Fences f = sp.load();
        if (f == null) {
            f = new Fences();
        }
        f.addZone(zone);
        sp.save(f);


        Log.i("Maps", "Made it passed f.addZone(z)");

        //This line is used to delete a geofence
        //f.deleteZone("asfddseafdsf");



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
