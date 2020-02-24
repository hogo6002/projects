package com.example.vritualbrowser.Entities;


import java.util.ArrayList;
import java.util.List;

/**
 * Create a class to represent the shopping mall, which will store all the stores and positions.
 * Furthermore, this will also store the test data for now, which will be removed after we link
 * database to navigation method.
 */
public class Mall {

    // a list store all the stores in this shopping mall.
    private List<Store> stores;

    // a list store all the store position of this shopping mall, which also represents
    // those position that user cannot path by.
    private List<Position> storePos;

    //direction for user from the begin to the destination.
    private List<String> direction;

    // some store examples, which will be used for test.
    Store store0;
    Store store6;
    Store store2;
    Store store10;
    Store store13;
    Store begin;
    Store end;

    // a route the store all the position that user should go in order to get the destination.
    Route route;


    /**
     * Create a new shopping mall, with the test stores.
     */
    public Mall() {
        stores = new ArrayList<>();
        storePos = new ArrayList<>();
        direction = new ArrayList<>();
        addStores();
    }

    public Mall(String mallInfo) {
        stores = new ArrayList<>();
        storePos = new ArrayList<>();
        direction = new ArrayList<>();
        begin = null;
        end = null;
        addStores(mallInfo);
    }

    public Store getBegin() {
        return begin;
    }

    public Store getEnd() {
        return end;
    }

    public void addStores(String stores) {
        stores = stores.split("\\^")[1];
        String[] storeInfo = stores.split("&");
        for (int i = 0; i < storeInfo.length; i++) {
            String name = storeInfo[i].split(":")[0];
            String position = storeInfo[i].split(":")[1];
            int posX = Integer.parseInt(position.split(",")[0]);
            int posY = Integer.parseInt(position.split(",")[1]);
            Position storePos = new Position(posX, posY);
            this.storePos.add(storePos);
            Store store = new Store(name, storePos);
            this.stores.add(store);
        }
    }

    /**
     * add test stores into store list, and their position into storePos list.
     */
    public void addStores() {
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
        store2 = new Store("store2", p2);
        Store store3 = new Store("store3", p3);
        Store store4 = new Store("store4", p4);
        Store store5 = new Store("store15", p5);
        Store store7 = new Store("store25", p6);
        Store store8 = new Store("store35", p7);
        Store store9 = new Store("store45", p6);

        Store store11 = new Store("store251", p10);
        Store store12 = new Store("store351", p11);
        store13 = new Store("store451", p12);

        store10 = new Store("store45", p8);
        store6 = new Store("store6", p9);
        stores.add(store0);
        stores.add(store1);
        stores.add(store2);
        stores.add(store3);
        stores.add(store4);
        stores.add(store5);
        stores.add(store6);
        stores.add(store7);
        stores.add(store8);
        stores.add(store9);
        stores.add(store10);
        stores.add(store11);
        stores.add(store12);
        stores.add(store13);
        storePos.add(p0);
        storePos.add(p1);
        storePos.add(p2);
        storePos.add(p3);
        storePos.add(p4);
        storePos.add(p5);
        storePos.add(p6);
        storePos.add(p7);
        storePos.add(p8);
        storePos.add(p9);
        storePos.add(p10);
        storePos.add(p11);
        storePos.add(p12);
    }

    /**
     * get all the stores of this mall.
     * @return stores - a list of store.
     */
    public List<Store> getStores() {
        return stores;
    }

    /**
     * To caculate the route for user from the begin to the destination.
     * @return positions - a list of position that user need to pass.
     */
    public List<Position> findRoute() {
        Position begin = this.begin.getPosition();
        Position dest = this.end.getPosition();
        route = new Route(begin, dest);
        List<Position> positions = new ArrayList<>();
        Position position;
        while (!(position = findNextPos(begin, dest)).equals(dest)) {
            positions.add(position);
            begin = position;

        }
        return positions;
    }

    /**
     * find the next position, from the current position to the destination.
     * @param begin the current position
     * @param dest the destination.
     * @return nextPos - next position.
     */
    public Position findNextPos(Position begin, Position dest) {
        Position nextPos = null;
        Position goRight = new Position(begin.getX() + 1, begin.getY());
        Position goLeft = new Position(begin.getX() - 1, begin.getY());
        Position goBottom = new Position(begin.getX(), begin.getY() + 1);
        Position goTop = new Position(begin.getX(), begin.getY() - 1);
        if (dest.getX() > begin.getX() && (!storePos.contains(goRight) || dest.equals(goRight))) {
            nextPos = goRight;
            direction.add("R");
        } else if (dest.getX() < begin.getX() && (!storePos.contains(goLeft) || dest.equals(goLeft))) {
            nextPos = goLeft;
            direction.add("L");
        } else if (dest.getY() > begin.getY() && (!storePos.contains(goBottom) || dest.equals(goBottom))) {
            nextPos = goBottom;
            direction.add("B");
        } else {
            nextPos = goTop;
            direction.add("T");
        }
        if (nextPos == null) {
            nextPos = dest;
        }
        System.out.println(begin.getX() + " y " + begin.getY());
        return nextPos;
    }

    /**
     * get the list of direction for user.
     * @return direction - a list of string to represent the direction.
     */
    public List<String> getDirection() {
        return direction;
    }

    /**
     * get the route for user
     * @return route
     */
    public Route getRoute() {
        return route;
    }

    public void addBegin(Store begin) {
        this.begin = begin;
    }

    public void addEnd(Store end) {
        this.end = end;
    }

}
