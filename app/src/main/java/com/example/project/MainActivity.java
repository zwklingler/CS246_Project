package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openMaps(View view) {
        Log.d("Intent Debug: ","Starting Maps Activity");
        //Create Intent
        Intent intent = new Intent(this, Maps.class);

        startActivity(intent);
    }

    public void openList(View view) {
        Log.d("Intent Debug: ","Starting List Activity");
        //Create Intent
        Intent intent = new Intent(this, zoneLists.class);

        startActivity(intent);
    }

    public void openSettings(View view) {
        Log.d("Intent Debug: ","Starting Settings Activity");
        //Create Intent
        Intent intent = new Intent(this, zoneSettings.class);

        startActivity(intent);
    }
}
