package com.example.vritualbrowser.Entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MallTest {
    private Mall mall;
    private Store store0;
    private Store store11;

    @Before
    public void setUp() throws Exception {
        Position p0 = new Position(0, 0);
        Position p1 = new Position(2, 0);
        Position p2 = new Position(2, 2);

        Position p10 = new Position(4, 0);
        Position p11 = new Position(4, 1);
        Position p12 = new Position(4, 2);

        Position p3 = new Position(0, 1);
        Position p4 = new Position(0, 2);
        Position p5 = new Position(0, 3);
        Position p6 = new Position(0, 4);
        Position p7 = new Position(0, 5);
        Position p8 = new Position(2, 1);
        Position p9 = new Position(4, 5);
        store0 = new Store("store0", p0);
        Store store1 = new Store("store1", p1);
        Store store2 = new Store("store2", p2);
        Store store3 = new Store("store3", p3);
        Store store4 = new Store("store4", p4);
        Store store5 = new Store("store15", p5);
        Store store7 = new Store("store25", p6);
        Store store8 = new Store("store35", p7);
        Store store9 = new Store("store45", p6);

        store11 = new Store("store251", p10);
        Store store12 = new Store("store351", p11);
        Store store13 = new Store("store451", p12);

        Store store10 = new Store("store45", p8);
        Store store6 = new Store("store6", p9);
        mall = new Mall();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getBegin() {
        mall.addBegin(store0);
        assertEquals(store0, mall.getBegin());
    }

    @Test
    public void getEnd() {
        mall.addEnd(store0);
        assertEquals(store0, mall.getEnd());
    }

    @Test
    public void addStores() {
        mall.addStores();
        assertEquals(store0, mall.getStores().get(0));
    }


    @Test
    public void getStores() {
        mall.addStores();
        assertEquals(store0, mall.getStores().get(0));
    }

    @Test
    public void findRoute() {
        mall.addBegin(store0);
        mall.addEnd(store11);
        List list = new ArrayList();
        for (int i = 0; i < mall.findRoute().size(); i++) {
            list.add(mall.findRoute().get(i));
        }

        assertEquals(list, mall.findRoute());
    }

    @Test
    public void findNextPos() {
        Position position = new Position(1, 0);
        assertEquals(position, mall.findNextPos(store0.getPosition(), store11.getPosition()));
    }

    @Test
    public void addBegin() {
        mall.addBegin(store0);
        assertEquals(store0, mall.getBegin());
    }

    @Test
    public void addEnd() {
        mall.addEnd(store0);
        assertEquals(store0, mall.getEnd());
    }
}