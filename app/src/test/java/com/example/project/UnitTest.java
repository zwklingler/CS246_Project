package com.example.project;

import android.content.Context;
import android.media.AudioManager;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    @Test
    public void checkLatitude() {
        Zone z = new Zone();
        z.setLatitude(55.51236323);
        assertEquals(55.51236323, z.getLatitude(), 0.000001);
    }

    @Test
    public void checkLongitude() {
        Zone z = new Zone();
        z.setLongitude(55.51236323);
        assertEquals(55.51236323, z.getLongitude(), 0.000001);
    }

    @Test
    public void checkName() {
        Zone z = new Zone();
        z.setName("Work");
        assertEquals("Work", z.getName());
    }

    @Test
    public void checkRadius() {
        Zone z = new Zone();
        z.setRadius(1002);
        assertEquals(1002, z.getRadius());
    }

    @Test
    public void addZone() {
        Zone z = new Zone();
        z.setRadius(500);
        z.setName("Work");
        z.setLongitude(123.4);
        z.setLatitude(53.65322);
        Fences f = new Fences();
        f.addZone(z);
        Zone test = f.getAllZones().get(0);
        assertEquals(test, z);


    }
}
