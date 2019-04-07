package com.example.project;

import android.content.Context;
import android.media.AudioManager;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.List;

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

}
