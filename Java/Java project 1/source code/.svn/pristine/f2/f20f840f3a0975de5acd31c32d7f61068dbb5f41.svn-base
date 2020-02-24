package pacman.util;

/**
 * Similar to a Point Class but instead called Position.
 * @ass1
 */
public class Position {

    // x value of this point
    private int x;
    // y value of this point
    private int y;

    /**
     * Creates a position at the given x and y coordinates.
     * @param x location.
     * @param y location.
     * @ass1
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the X axis location.
     * @return x position.
     * @ass1
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Y axis location.
     * @return y position.
     * @ass1
     */
    public int getY() {
        return y;
    }

    /**
     * Calculates the Euclidean distance from this point to the given
     * other point.
     * @param other point used to calculate the euclidean distance.
     * @return the euclidean distance.
     * @ass1
     */
    public double distance(Position other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    /**
     * Adds two positions together.
     * @param other position to add to this one.
     * @return This position + other.
     * @ass1
     */
    public Position add(Position other) {
        return new Position(x + other.x, y + other.y);
    }

    /**
     * Multiplies by a factor on the x and y axis.
     * @param factor to multiple the axis by.
     * @return a new position with the x axis scaled by factor and y
     * axis scaled by factor.
     * @ass1
     */
    public Position multiply(int factor) {
        return new Position(x * factor, y * factor);
    }

    /**
     * Checks if two positions are equal.
     *
     * @param other object to compare against.
     * @return true if x == this.x and also y == this.y, false otherwise.
     * @ass1
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Position)) {
            return false;
        }
        
        Position position = (Position) other;
        
        return x == position.x && y == position.y;
    }

    /**
     * Calculates the hash of the position.
     *
     * <p>
     *     For two objects that are equal the hash should also
     *     be equal. For two objects that are not equal the hash
     *     does not have to be different.
     * </p>
     *
     * As an example:
     *
     * <pre>{@code
     * Position uq = new Position(120, 78);
     * Position prentice = new Position(120, 78);
     * Position gatton = new Position(80, 130);
     *
     * uq.equals(prentice) // returns true
     * uq.hashCode() == prentice.hashCode(); // returns true
     *
     * uq.equals(gatton) // returns false
     * uq.hashCode() == gatton.hashCode() // can return true or false
     *
     * }</pre>
     * @return hash of this position.
     * @ass1
     */
    @Override
    public int hashCode() {
        return x + y;
    }

    /**
     * Returns a comma-seperated string representation of a Position. 
     * Format: "x,y"
     * Example: "3,4"
     * @return "x,y"
     * @ass2
     */
    @Override
    public String toString() {
        return String.format("%d,%d", x, y);
    }
}
