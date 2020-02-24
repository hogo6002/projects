package com.example.vritualbrowser.Entities;

/**
 * Store class, which store the store name and store position.
 */
public class Store {

    // store name
    private String name;

    // store position
    private Position position;

    /**
     * Construct a new store with the given store name, and store position.
     * @param name store name
     * @param position store position
     */
    public Store(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    /**
     * get the name of this store
     * @return name - store name
     */
    public String getName() {
        return name;
    }

    /**
     * get the position of this store
     * @return position - store position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * compare two stores, by comparing their name.
     * @param o other store that needs to compare with.
     * @return true - if their share the same store name.
     *          false - if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this.getName().equals(((Store) o).getName())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Override hashcode method.
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int cap=55544;
        return (int) name.charAt(0) * cap;
    }
}
