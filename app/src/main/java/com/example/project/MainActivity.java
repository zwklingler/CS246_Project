package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Startup activity for the application. Contains several on click functions for
 * various buttons that start new activities.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Required for activities
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Sends intent to start maps activity.
     * @param view Required for having an on click event for a button.
     */
    public void openMaps(View view) {
        Log.d("Intent Debug: ","Starting Maps Activity");
        //Create Intent
        Intent intent = new Intent(this, Maps.class);

        startActivity(intent);
    }

    /**
     * Sends intent to start Zone Lists activity.
     * @param view Required for having an on click event for a button.
     */
    public void openList(View view) {
        Log.d("Intent Debug: ","Starting List Activity");
        //Create Intent
        Intent intent = new Intent(this, zoneLists.class);

        startActivity(intent);
    }

    /**
     * Sends intent to start Zone Settings activity.
     * @param view Required for having an on click event for a button.
     */
    public void openSettings(View view) {
        Log.d("Intent Debug: ","Starting Settings Activity");
        //Create Intent
        Intent intent = new Intent(this, zoneSettings.class);

        startActivity(intent);
    }
}
