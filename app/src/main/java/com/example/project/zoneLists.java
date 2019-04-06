package com.example.project;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillValue;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class zoneLists extends AppCompatActivity {

    private Fences fences;

    /**
     * Creates a list of zones to be deleted, uses sharedPrefs to load the zones
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_lists);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView zoneList = findViewById(R.id.zoneListView);

        SharedPrefs sp = new SharedPrefs(this);
        fences = sp.load();
        fences.setContext(this);
        final List<Zone> zones = new ArrayList<>(fences.getAllZones());


        final ArrayAdapter<Zone> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, zones);

        zoneList.setAdapter(adapter);

        zoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fences.deleteZone(zones.get(position).getName());
                zones.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Saves deletions and goes back to the main activity
     */
    public void saveList(View view) {
        SharedPrefs sp = new SharedPrefs(this);
        sp.save(fences);
        finish();
    }
}
