package com.example.project;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.MediaStore;
import android.util.Log;

import static android.media.AudioManager.STREAM_RING;

public class ChangeRinger {
    private AudioManager am;

    ChangeRinger(Context context) {
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
        //This turns of Do Not Disturb
        //am.setRingerMode(2);

        //Unmute Ringer
        am.adjustStreamVolume(STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);

    }
}
