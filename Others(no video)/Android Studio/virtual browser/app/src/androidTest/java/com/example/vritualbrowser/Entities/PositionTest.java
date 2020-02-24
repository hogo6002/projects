package com.example.vritualbrowser.Entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {
    private Position position1;
    private Position position2;
    private Position position3;

    @Before
    public void setUp() throws Exception {
        position1 = new Position(1, 1);
        position2 = new Position(1, 1);
        position3 = new Position(1, 2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getX() {
        assertEquals(1, position1.getX());
    }

    @Test
    public void getY() {
        assertEquals(1, position1.getY());
    }

    @Test
    public void equals1() {
        assertEquals(position1, position2);
        assertNotEquals(position1, position3);
    }
}