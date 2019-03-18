package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean isSaved = extras.getBoolean("isSaved");
            if (isSaved == true) {
                Toast.makeText(this, "Your quiet zone has been saved",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void openMaps(View view) {
        Log.i("Intent Debug: ","Starting Maps Activity");
        //Create Intent
        Intent intent = new Intent(this, Maps.class);

        startActivity(intent);
    }
}
