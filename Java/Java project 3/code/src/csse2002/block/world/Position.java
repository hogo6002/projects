package csse2002.block.world;

import java.util.Objects;

/**
 * Represents the position of a {@link Tile Tile}
 * in the {@link SparseTileArray SparseTileArray}.
 * @serial exclude
 */
public class Position implements Comparable<Position> {
    private int x;
    private int y;

    /**
     * Construct a position for (x, y).
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x coordinate.
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate.
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * (see
     * <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html">
     * https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html</a>) <br>
     * Two Positions are equal if {@literal getX() == other.getX() &&}
     * {@literal getY() == other.getY()}
     * @param obj the object to compare to
     * @return true if obj is an instance of Position and if obj.x == x and
     *         obj.y == y.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  Position)) {
            return false;
        }
        Position other = (Position) obj;
        return other.x == x && other.y == y;
    }

    /**
     * Compute a hashCode that
     * meets the contract of Object.hashCode <br>
     * (see
     * <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html">
     * https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html</a>)
     * @return a suitable hashcode for the Position
     */
    //@Override
    //public int hashCode() {
    //    return x + y;
    //}


    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Compare this position to another position. <br>
     * return
     * <ul>
     *     <li> {@literal -1 if getX() < other.getX()} </li>
     *     <li> {@literal -1 if getX() == other.getX()
     *          and getY() < other.getY()} </li>
     *     <li> {@literal 0 if getX() == other.getX()
     *          and getY() == other.getY()} </li>
     *     <li> {@literal 1 if getX() > other.getX()} </li>
     *     <li> {@literal 1 if getX() == other.getX()
     *          and getY() > other.getY()} </li>
     * </ul>
     * @param other the other Position to compare to
     * @return -1, 0, or 1 depending on conditions above
     */
    @Override
    public int compareTo(Position other) {
        if (getX() < other.getX()) {
            return -1;
        } else if (getX() > other.getX()) {
            return 1;
        } else if (getY() < other.getY()) {
            return -1;
        } else if (getY() > other.getY()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Convert this position to a string. <br>
     * String should be {@literal "(<x>, <y>)"} where
     * {@literal <x>} is the value returned by getX() and
     * {@literal <y>} is the value returned by getY(). <br>
     * Note the space following the comma.
     * @return a string representation of the position {@literal "(<x>, <y>)"}
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
