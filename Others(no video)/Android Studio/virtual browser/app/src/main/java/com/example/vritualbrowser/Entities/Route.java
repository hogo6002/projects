package com.example.vritualbrowser.Entities;

/**
 * route class, which will show the begin and destination of this route.
 */
public class Route {

    // the begin position
    private Position begin;

    // the destination position
    private Position dest;

    /**
     * construct a new route with the given begin and destination point.
     * @param begin the start point
     * @param dest the end point
     */
    public Route(Position begin, Position dest) {
        this.dest = dest;
        this.begin = begin;
    }

    /**
     * get the start point
     * @return begin start point
     */
    public Position getBegin() {
        return begin;
    }

    /**
     * get the end point
     * @return dest end point
     */
    public Position getDest() {
        return dest;
    }
}
