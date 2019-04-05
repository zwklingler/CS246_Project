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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_lists);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView zoneList = findViewById(R.id.zoneListView);

        //final List<Zone> zones = new ArrayList<>();

        SharedPrefs sp = new SharedPrefs(this);
        fences = sp.load();
        fences.setContext(this);
        final List<Zone> zones = new ArrayList<Zone>(fences.getAllZones());

        /*
        Zone zone = new Zone();
        zone.setName("coolZone");
        zones.add(zone);

        Zone zone2 = new Zone();
        zone2.setName("Lame Zone");
        zones.add(zone2);
*/

        final ArrayAdapter<Zone> adapter = new ArrayAdapter<Zone>(this, android.R.layout.simple_list_item_1, zones);

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

    public void saveList(View view) {
        SharedPrefs sp = new SharedPrefs(this);
        sp.save(fences);
        finish();
    }
}
