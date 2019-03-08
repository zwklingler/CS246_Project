package com.example.project;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import static android.media.AudioManager.STREAM_RING;

public class ChangeRinger extends IntentService {
    private AudioManager am;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ChangeRinger(String name) {
        super(name);
    }

    public void createAM(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        else {
            // Ask the user to grant access
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
            context.startActivity(intent);
        }

    }

    public void changeRinger() {
        //This should make the Ringer Silent, but for some reason it turns on Do Not Disturb (Doesn't work as expected)
        //am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

        //Set Ringer Volume to 0 (works as expected)
        am.adjustStreamVolume(STREAM_RING, AudioManager.ADJUST_MUTE, 0);

        //Log.d("Ringer Test: ", String.valueOf(am.getRingerMode()));
        //am.setRingerMode(2);

    }

    public void revertRinger() {
        //This turns off Do Not Disturb
        //am.setRingerMode(2);

        //Unmute Ringer
        am.adjustStreamVolume(STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);

    }

    //Called when a geofence is triggered
    @Override
    protected void onHandleIntent(Intent intent) {
        //Passes the context to initialize the Audio Manager Item
        createAM(this);
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = "There was an error with the geofences";
            Log.e("Geofence", errorMessage);
            return;
        }
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        //If they entered the zone, silence the phone
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            changeRinger();
        }
        //If they left the zone, turn on revert the volume
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            revertRinger();
        }
    }
}
