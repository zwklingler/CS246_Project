package com.example.project;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for removing geofences from the loaded fences object,
 * and then re-saving fences into shared preferences.
 */
public class ZoneLists extends AppCompatActivity {

    private Fences fences;

    /**
     * Creates a list of zones to be deleted, uses sharedPrefs to load the zones.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_lists);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView zoneList = findViewById(R.id.zoneListView);

        //Load Fences from Shared Prefs
        SharedPrefs sp = new SharedPrefs(this);
        fences = sp.load();
        fences.setContext(this);
        final List<Zone> zones = new ArrayList<>(fences.getAllZones());


        final ArrayAdapter<Zone> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, zones);

        zoneList.setAdapter(adapter);

        zoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Remove zone from fences and delete it on List
                fences.deleteZone(zones.get(position).getName());
                zones.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Saves deletions and goes back to the main activity.
     */
    public void saveList(View view) {
        //Save changes to Shared Prefs
        SharedPrefs sp = new SharedPrefs(this);
        sp.save(fences);

        //Go back to Main Activity
        finish();
        Toast.makeText(this, "The Selected Zones have been Removed", Toast.LENGTH_SHORT).show();

    }
}
