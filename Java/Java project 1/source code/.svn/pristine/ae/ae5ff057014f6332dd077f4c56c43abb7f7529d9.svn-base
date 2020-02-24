package pacman.util;

/**
 * Direction represents directions in a 2D plane.
 *
 * Each direction stores a relative position:
 *
 * <table border="1">
 *   <caption>Enum Definitions</caption>
 *   <tr>
 *     <td> Direction</td>
 *     <td> X </td>
 *     <td> Y </td>
 *   </tr>
 *   <tr><td>LEFT</td><td>-1</td><td>0</td></tr>
 *   <tr><td>RIGHT</td><td>1</td><td>0</td></tr>
 *   <tr><td>UP</td><td>0</td><td>-1</td></tr>
 *   <tr><td>DOWN</td><td>0</td><td>1</td></tr>
 * </table>
 * @ass1
 */
public enum Direction {
    /**
     * Facing to the left.
     */
    LEFT(new Position(-1, 0)),
    /**
     * Facing to the right.
     */
    RIGHT(new Position(1, 0)),
    /**
     * Facing up.
     */
    UP(new Position(0, -1)),
    /**
     * Facing down.
     */
    DOWN(new Position(0, 1));

    // direction offset
    private Position offset;

    /**
     * Creates a Direction with the given position offset.
     * @param offset to be set.
     * @ass1
     */
    Direction(Position offset) {
        this.offset = offset;
    }

    /**
     * Gets the offset associated with this direction.
     * @return relative position offset.
     * @ass1
     */
    public Position offset() {
        return this.offset;
    }

    /**
     * Gets the opposite direction to this direction.
     * @return the opposite direction e.g. up returns down, left
     * returns right. If no opposite direction exists then return
     * this direction.
     * @ass1
     */
    public Direction opposite() {
        switch (this) {
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            default:
                return this;
        }
    }
}
