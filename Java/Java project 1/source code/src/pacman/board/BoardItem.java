package pacman.board;

/**
 * BoardItem
 *
 * <p>This enum defines different items that are placed on the board.
 * Items may have a pickup score if the item can be picked up. Items
 * must define whether they are path-able.</p>
 *
 * <table border="1">
 *   <caption>Enum Definitions</caption>
 *   <tr>
 *     <td> BoardItem </td>
 *     <td> Pathable </td>
 *     <td> Score </td>
 *     <td> Char Key </td>
 *   </tr>
 *   <tr>
 *       <td>NONE</td><td>true</td><td>0</td>
 *       <td>'0', note this is the number 0</td>
 *   </tr>
 *   <tr><td>WALL</td><td>false</td><td>0</td><td>'X'</td></tr>
 *   <tr><td>DOT</td><td>true</td><td>10</td><td>'1'</td></tr>
 *   <tr><td>BIG_DOT</td><td>true</td><td>15</td><td>'B'</td></tr>
 *   <tr><td>BIG_DOT_SPAWN</td><td>true</td><td>0</td><td>'b'</td></tr>
 *   <tr><td>GHOST_SPAWN</td><td>true</td><td>0</td><td>'$'</td></tr>
 *   <tr><td>PACMAN_SPAWN</td><td>true</td><td>0</td><td>'P'</td></tr>
 * </table>
 *
 * @ass1
 */
public enum BoardItem {
    /**
     * A nothing item ( Empty )
     */
    NONE('0', true),
    /**
     * A wall tile
     */
    WALL('X', false),
    /**
     * A dot
     */
    DOT('1', true, 10),
    /**
     * A big dot
     */
    BIG_DOT('B', true, 15),
    /**
     * A big dot spawn point
     */
    BIG_DOT_SPAWN('b', true),
    /**
     * A spawn point for GHOSTS
     */
    GHOST_SPAWN('$', true),
    /**
     * A spawn point for PACMAN
     */
    PACMAN_SPAWN('P', true);

    // char representation of this item
    private char key;
    // score value of this item
    private int score;
    // whether this item is pathable
    private boolean pathable;

    /**
     * single argument board item constructor with the required
     * path-able attribute.
     *
     * @param key defines the character key for this item.
     * @param pathable defines whether this item can be walked on
     *                 (true), or blocks movement (false)
     * @ass1
     */
    BoardItem(char key, boolean pathable) {
        this(key, pathable, 0);
    }

    /**
     * Double argument board item constructor, for items that
     * give points when obtained.
     *
     * @param key defines the character key for this item.
     * @param pathable defines whether this item can be walked on
     *                 (true), or blocks movement (false)
     * @param score    defines the number of points gained on pickup
     * @ass1
     */
    BoardItem(char key, boolean pathable, int score) {
        this.key = key;
        this.pathable = pathable;
        this.score = score;
    }

    /**
     * gets the score of the item.
     *
     * @return the score associated with an item.
     * @ass1
     */
    public int getScore() {
        return this.score;
    }

    /**
     * gets the path-able nature of the item.
     *
     * @return whether the item is path-able.
     * @ass1
     */
    public boolean getPathable() {
        return pathable;
    }

    /**
     * gets the character key of the item.
     *
     * @return the character key associated with the item.
     * @ass1
     */
    public char getChar() {
        return this.key;
    }

    /**
     * Takes a character and returns the associated BoardItem as
     * presented in the Enum comment's "Enum definition" table.
     * see {@link pacman.board.BoardItem}
     *
     * @param key a character that represents the board item. The
     *            acceptable characters are defined in the Enum's
     *            "Enum definition" table.
     * @return the board Item associated with the character
     * @throws IllegalArgumentException if the character is not part
     * of the supported Items
     * @ass1
     */
    public static BoardItem getItem(char key) throws IllegalArgumentException {
        for (BoardItem item : values()) {
            if (key == item.getChar()) {
                return item;
            }
        }

        throw new IllegalArgumentException(
                "Provided character is not supported");
    }
}
