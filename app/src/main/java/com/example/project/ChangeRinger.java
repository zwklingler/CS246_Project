package com.example.project;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import static android.media.AudioManager.STREAM_RING;

/**
 * Changes the ringer volume using an Audio Manager.
 */
public class ChangeRinger extends IntentService {
    private AudioManager am;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ChangeRinger() {
        super("Change Ringer");
    }

    /**
     * Creates an Audio Manager object to alter the ringer.
     * @param context Context needed for altering the volume.
     */
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

    /**
     * Mutes volume, Turns on Vibrate, or Changes Volume to Specified Amount.
     */
    public void changeRinger() {
        //Load vibrate and volume from shared prefs
        SharedPreferences pref = this.getSharedPreferences("Geofences", MODE_PRIVATE);
        double doubleVolume = pref.getInt("Volume", 0) * 0.01;
        int volume = (int) (doubleVolume * am.getStreamMaxVolume(STREAM_RING));
        boolean vibrate = pref.getBoolean("Vibrate", false);
        //Do they want volume or vibrate instead of muting
        if (vibrate) {
            Log.i("Altering Volume: ","Setting to Vibrate");
            am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }
        else if (volume >= 1 && volume <= am.getStreamMaxVolume(STREAM_RING)) {
            Log.i("Altering Volume: ","Setting Volume to " + volume);
            am.setStreamVolume(STREAM_RING, volume, 0);
        }
        else {
            //Set Ringer Volume to 0 (works as expected)
            Log.i("Altering Volume: ","Muting Volume");
            am.adjustStreamVolume(STREAM_RING, AudioManager.ADJUST_MUTE, 0);
        }
    }

    /**
     * Changes ringer back to volume before it was muted.
     */
    public void revertRinger() {
        SharedPreferences pref = this.getSharedPreferences("Geofences", MODE_PRIVATE);
        boolean vibrate = pref.getBoolean("Vibrate", false);
        //If Vibrate is on revert back using this way
        if (vibrate) {
            //Go back from Vibrate
            Log.i("Altering Volume: ","Reverting From Vibrate");
            am.setRingerMode(2);
        }
        else {
            //Unmute Ringer
            Log.i("Altering Volume: ","Reverting Volume");
            am.adjustStreamVolume(STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
        }


    }

    //Called when a geofence is triggered

    /**
     * Determines whether someone entered or exited the geofence, and mutes/unmutes the volume
     * accordingly.
     * @param intent Intent is passed when geofence detects an entrance or exit.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        //Passes the context to initialize the Audio Manager Item
        createAM(this);
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        Log.i("ChangeRinger: ", "Made it to On Handle Intent");
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
