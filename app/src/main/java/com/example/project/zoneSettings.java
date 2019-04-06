package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class zoneSettings extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private SeekBar volumeBar;
    private Switch disturbSwitch;
    private TextView volumeNumber;
    private int volume;
    private boolean doNotDisturb;

    /**
     * Creates the settings view with a set volumebar seekbar and a switch for do not disturb
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        disturbSwitch = findViewById(R.id.doNotDisturbSwitch);
        disturbSwitch.setOnCheckedChangeListener(this);

        volumeNumber = findViewById(R.id.volumeNumber);

        volumeBar = findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(this);

        doNotDisturb = false;
    }

    /**
     * When using the seekbar this function changes a text view to indicate volume level
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        volumeNumber.setText(String.valueOf(progress));
    }

    /**
     * Function call when the user starts using the seekbar, included with listener implementation
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Function call when the user stops using the seekbar, this sets the volume to a specified level
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        volume = seekBar.getProgress();
    }

    /**
     * Saves the current settings for the zones volume and do not disturb
     */
    public void save(View view) {
        //Save Settings
        //int volume & boolean doNotDisturb
        finish();
    }

    /**
     * When the switch is changed this function is called. Determines if Do Not Disturb is on or off
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        doNotDisturb = isChecked;
    }
}


