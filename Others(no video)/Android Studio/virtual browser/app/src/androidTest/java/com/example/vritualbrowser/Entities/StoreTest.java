package com.example.vritualbrowser.Entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoreTest {
    private Store store1;
    private Store store2;
    private Store store3;
    private Store store4;
    private Position position1;
    private Position position2;

    @Before
    public void setUp() throws Exception {
        position1 = new Position(1, 1);
        position2 = new Position(2, 2);
        store1 = new Store("dj", position1);
        store2 = new Store("dj", position1);
        store3 = new Store("jb", position1);
        store4 = new Store("dj", position2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() {
        assertEquals("dj", store1.getName());
    }

    @Test
    public void getPosition() {
        assertEquals(position1, store1.getPosition());
    }

    @Test
    public void equals1() {
        assertEquals(store1, store2);
    }

    // same position, different store name
    @Test
    public void equals2() {
        assertNotEquals(store1, store3);
    }
}