package com.example.vritualbrowser.Entities;

/**
 * Position to represent the coordinate of each store.
 */
public class Position {
    // x axis
    private int x;
    // y axis
    private int y;

    /**
     * construct a new position with the given x and y.
     * @param x
     * @param y
     */
    protected Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * get the store position of x axis
     * @return x
     */
    protected int getX() {
        return x;
    }

    /**
     * get the store position of y axis
     * @return y
     */
    protected int getY() {
        return y;
    }

    /**
     * two position will be equal, if they are in the same x axis and y axis.
     * @param o a new position, that this position needs to compare with.
     * @return true - if two position are same.
     *          false - if not.
     */
    @Override
    public boolean equals(Object o) {
        try {
            Position p=(Position)o;
            return ((p.x==this.x) && (p.y==this.y));
        } catch (ClassCastException cce) {
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
        return (((x%cap)*111%cap)+(y%cap))%cap;
    }
}

