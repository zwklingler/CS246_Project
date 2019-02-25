package com.example.project;

import java.lang.Object;

public class Zone {
    private double latitude;
    private double longitude;
    private int radius;
    private String name;
    private int ringer;
    private int volume;


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getRadius() {
        return radius;
    }

    public String getName() {
        return name;
    }

    public int getRinger() {
        return ringer;
    }

    public int getVolume() {
        return volume;
    }

    public void createGeofence() {

    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRinger(int ringer) {
        this.ringer = ringer;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
