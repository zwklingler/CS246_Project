package com.example.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for altering the app's settings. It stores values into variables, and upon pressing the button,
 * it will save those variables into shared preferences.
 */
public class ZoneSettings extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private SeekBar volumeBar;
    private Switch disturbSwitch;
    private TextView volumeNumber;
    private int volume;
    private boolean vibrate;

    /**
     * Creates the settings view with a set volume bar seek bar and a switch for vibrate.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        disturbSwitch = findViewById(R.id.vibrateSwitch);
        disturbSwitch.setOnCheckedChangeListener(this);

        volumeNumber = findViewById(R.id.volumeNumber);

        volumeBar = findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(this);

        vibrate = false;
    }

    /**
     * When using the seekbar this function changes a text view to indicate volume level.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        volumeNumber.setText(String.valueOf(progress));
    }

    /**
     * Function call when the user starts using the seekbar, included with listener implementation.
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Function call when the user stops using the seekbar, this sets the volume to a specified level.
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        volume = seekBar.getProgress();
    }

    /**
     * Saves the current settings for the zones volume and vibrate.
     */
    public void save(View view) {
        //Save Settings
        //int volume & boolean vibrate
        SharedPreferences pref = this.getSharedPreferences("Geofences", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Log.i("zoneSettings: ", "Vibrate: " + vibrate);
        Log.i("zoneSettings: ", "Volume: " + volume);
        editor.putBoolean("Vibrate", vibrate);
        editor.putInt("Volume", volume);
        editor.apply();

        //Go back to Main Activty
        finish();
        Toast.makeText(this, "The Settings have been Saved", Toast.LENGTH_SHORT).show();

    }

    /**
     * When the switch is changed this function is called. Determines if Vibrate is on or off.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        vibrate = isChecked;
    }
}


