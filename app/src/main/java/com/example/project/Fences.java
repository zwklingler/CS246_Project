package com.example.project;

import java.util.ArrayList;
import java.util.List;

public class Fences {

    private List<Zone> allZones;


    Fences() {
        allZones = new ArrayList<>();
    }
    public void deleteZone(String id) {

    }

    public void addZone(Zone zone) {
        allZones.add(zone);
    }

    public List<Zone> getAllZones() {
        return allZones;
    }

    public void save() {

    }

    public void load() {

    }
}
