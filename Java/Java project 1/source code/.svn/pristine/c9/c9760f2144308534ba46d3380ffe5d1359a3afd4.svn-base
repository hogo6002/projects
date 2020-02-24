package pacman.board;

import java.util.StringJoiner;
import pacman.util.Position;

import java.util.Arrays;

/**
 * PacmanBoard
 * <p>
 * Represents the Pac Man game board.
 * The board can be any size, it is set out as a grid with each space
 * containing only one {@link pacman.board.BoardItem}.
 *
 * Game boards are by default surrounded by a BoardItem.WALL with
 * every other space being BoardItem.NONE.
 *
 * Example:
 *
 * a board with width 1 and height 1 would look like this:
 * <pre>
 *     X
 * </pre>
 *
 * A board with width 4 and height 4 would look like this:
 *
 * <pre>
 *     XXXX
 *     X00X
 *     X00X
 *     XXXX
 * </pre>
 *
 * The coordinate positions for the board is the top left position is
 * (0, 0) and the bottom right position is (getWidth-1, getHeight-1).
 *
 * @ass1
 */
public class PacmanBoard {

    // width of the board
    private int width;
    // height of the board
    private int height;
    // the board grid
    private BoardItem[][] board;

    /**
     * Constructor taking the width and height creating a board that
     * is filled with BoardItem.NONE except a 1 block wide border wall
     * around the entire board ( BoardItem.WALL ).
     *
     * @param height the vertical size of the board which is greater
     *               than zero.
     * @param width  the horizontal size of the board which is
     *               greater than zero.
     * @throws IllegalArgumentException when height || width is less
     * than or equal to 0.
     * @ass1
     */
    public PacmanBoard(int width, int height) throws
            IllegalArgumentException {
        
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException();
        }
        
        this.width = width;
        this.height = height;
        this.board = new BoardItem[width][height];

        // setup board with walls around the edge, nothing on the inside
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j == 0 || j == (height - 1) ||
                        i == 0 || i == (width - 1)) {
                    board[i][j] = BoardItem.WALL;
                } else {
                    board[i][j] = BoardItem.NONE;
                }
            }
        }
    }

    /**
     * Constructor taking an existing PacmanBoard and making a deep
     * copy. A deep copy should have the same getWidth, getHeight and board
     * as the given board. When a change is made to the other board
     * this should not change this copy.
     *
     * @param other copy of an existing PacmanBoard.
     * @throws NullPointerException if copy is null.
     * @ass1
     */
    public PacmanBoard(PacmanBoard other) throws NullPointerException {
        if (other == null) {
            throw new NullPointerException();
        }
        
        this.width = other.width;
        this.height = other.height;
        this.board = new BoardItem[width][height];
        
        for (int i = 0; i < width; i++) {
            this.board[i] = other.board[i].clone();
        }
    }

    /**
     * Gets the width of the board
     *
     * @return width of the game board
     * @ass1
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the board
     *
     * @return height of the board
     * @ass1
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets a tile on the board to an item.
     *
     * If the item to be set is a BoardItem.PACMAN_SPAWN and the board
     * already contains a BoardItem.PACMAN_SPAWN then the old
     * BoardItem.PACMAN_SPAWN should be made a BoardItem.NONE.
     *
     * If the item to be set is a BoardItem.GHOST_SPAWN and the board
     * already contains a BoardItem.GHOST_SPAWN then the old
     * BoardItem.GHOST_SPAWN should be made a BoardItem.NONE.
     *
     * @param item     the board item that is to be placed at the position.
     * @param position the portion to place the item.
     * @throws IndexOutOfBoundsException when the position trying to
     *          be set is not within the board.
     * @throws NullPointerException when the position or item is null.
     * @ass1
     * @see pacman.util.Position
     */
    public void setEntry(Position position, BoardItem item) throws
            IndexOutOfBoundsException, NullPointerException {
        
        if (position == null || item == null) {
            throw new NullPointerException();
        }

        // check position is valid
        if (position.getX() < 0 || position.getY() < 0 ||
                position.getX() >= width || position.getY() >= height) {
            throw new IndexOutOfBoundsException();
        }

        // ensure only one PACMAN_SPAWN/GHOST_SPAWN on board at a time
        if (item == BoardItem.PACMAN_SPAWN) {
            Position spawn = getPacmanSpawn();
            if (spawn != null) {
                setEntry(spawn, BoardItem.NONE);
            }
        } else if (item == BoardItem.GHOST_SPAWN) {
            Position spawn = getGhostSpawn();
            if (spawn != null) {
                setEntry(spawn, BoardItem.NONE);
            }
        }

        board[position.getX()][position.getY()] = item;
    }

    /**
     * Returns what item the board has on a given position.
     *
     * @param position wanting to be checked
     * @return BoardItem at the location given.
     * @throws IndexOutOfBoundsException when the position
     *                                   is not within the board.
     * @throws NullPointerException if position is null.
     * @ass1
     */
    public BoardItem getEntry(Position position) throws
            IndexOutOfBoundsException, NullPointerException {
        
        if (position == null) {
            throw new NullPointerException();
        }
        
        return board[position.getX()][position.getY()];
    }

    /**
     * Tries to eat a dot off the board and returns the item that it
     * ate/tried to eat. If a BoardItem.DOT is eaten then it is replaced
     * with a BoardItem.NONE. If a BoardItem.BIG_DOT is eaten then it
     * is replaced with a BoardItem.BIG_DOT_SPAWN. If the item is
     * any other BoardItem then do nothing and return the item.
     *
     * @param position to eat.
     * @return the item that was originally the position before trying
     * to eat.
     * @throws NullPointerException if position is null.
     * @throws IndexOutOfBoundsException when the position trying to
     * be eaten is not within the board.
     * @ass1
     */
    public BoardItem eatDot(Position position) throws
            IndexOutOfBoundsException, NullPointerException {
        
        BoardItem item = getEntry(position);

        if (item == BoardItem.BIG_DOT) {
            setEntry(position, BoardItem.BIG_DOT_SPAWN);
        } else if (item == BoardItem.DOT) {
            setEntry(position, BoardItem.NONE);
        }

        return item;
    }

    /**
     * Get the spawn position for the ghosts.
     *
     * @requires board to contain 0 or 1 GHOST_SPAWN's
     * @return the position of the ghost spawn or null if none found.
     * @ass1
     */
    public Position getGhostSpawn() {
        return getSpawn(BoardItem.GHOST_SPAWN);
    }

    /**
     * Get the spawn position for pacman.
     *
     * @requires board to contain 0 or 1 PACMAN_SPAWN's
     * @return the postion of pacmans spawn or null if none found.
     * @ass1
     */
    public Position getPacmanSpawn() {
        return getSpawn(BoardItem.PACMAN_SPAWN);
    }

    /*
     * Gets the portion of a given spawn type.
     */
    private Position getSpawn(BoardItem spawnType) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (board[x][y] == spawnType) {
                    return new Position(x, y);
                }
            }
        }
        
        return null;
    }


    /**
     * Checks if the board contains any pickup items.
     *
     * @return true if the board does not contain any DOT's or
     * BIG_DOT's.
     * @ass1
     */
    public boolean isEmpty() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (board[x][y] == BoardItem.DOT ||
                        board[x][y] == BoardItem.BIG_DOT) {
                    return false;
                }
            }
        }
        
        return true;
    }

    /**
     * Resets the board to place a DOT in every position that has no
     * item ( NONE BoardItem ) and respawns BIG_DOT's in the
     * BIG_DOT_SPAWN locations. Leaves walls, pacman spawns and ghost
     * spawns intact.
     * @ass1
     */
    public void reset() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                if (board[x][y] == BoardItem.NONE) {
                    board[x][y] = BoardItem.DOT;
                } else if (board[x][y] == BoardItem.BIG_DOT_SPAWN) {
                    board[x][y] = BoardItem.BIG_DOT;
                }
            }
        }
    }

    /**
     * Checks if another object instance is equal to this instance. 
     * Boards are equal if they have the same dimensions (width, height) and
     * have equal items for all board positions. 
     * @return true if same, false otherwise.
     * @ass2
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (!(o instanceof PacmanBoard)) {
            return false;
        }

        PacmanBoard otherBoard = (PacmanBoard) o;

        return width == otherBoard.width && height == otherBoard.height 
                && Arrays.deepEquals(board, otherBoard.board);
    }

    /**
     * For two objects that are equal the hash should also
     * be equal. For two objects that are not equal the hash
     * does not have to be different.
     * @return hash of PacmanBoard.
     * @ass2
     */
    @Override
    public int hashCode() {
        return (31 * width) + (33 * height) + Arrays.deepHashCode(board);
    }

    /**
     * Creates a multiline string ( using System.lineSeparator() as newline ) 
     * that is a printout of each index of the board with the character
     * key.
     *
     * Example of a board with height 3 width 4.
     *
     * <pre>
     *     XXXX
     *     X00X
     *     XXXX
     * </pre>
     *
     * Note: the lines are not indented but may show in the JavaDoc as
     * such. There are no spaces. The last line does not have a newline.
     *
     * @ass2
     * @return board as a multiline string.
     */
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        
        for (int y = 0; y < height; y++) {
            StringBuilder rowBuilder = new StringBuilder();
            
            for (int x = 0; x < width; x++) {
                rowBuilder.append(getEntry(new Position(x, y)).getChar());
            }
            
            joiner.add(rowBuilder.toString());
        }
        
        return joiner.toString();
    }
}
