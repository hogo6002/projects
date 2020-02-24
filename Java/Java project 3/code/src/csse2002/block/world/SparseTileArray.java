package csse2002.block.world;

import java.util.*;

/**
 * A sparse representation of tiles in an Array. <br>
 * Contains {@link Tile Tiles}s stored with an
 * associated {@link Position Position} (x, y) in a map. <br>
 *
 * @serial exclude
 */
public class SparseTileArray {

    // lookup tiles by position
    private HashMap<Position, Tile> tileMap;

    // a set of tiles in the order in
    // a breadth-first search order
    private List<Tile> orderedTiles;

    /**
     * Constructor for a SparseTileArray.
     * Initializes an empty SparseTileArray, such that
     * getTile(new Position(x, y)) returns null for any x and y and
     * getTiles() returns an empty list.
     */
    public SparseTileArray() {
        reset();
    }

    /**
     * Get the tile at position at (x, y), given by position.getX() and
     * position.getY(). Return null if there is no tile at (x, y). <br>
     * Hint: Construct a {@literal Map<Position, Tile>}
     * in addLinkedTiles to allow looking up tiles
     * by position.
     * @param position the tile position
     * @return the tile at (x, y) or null if
     *         no such tile exists.
     * @require position != null
     */
    public Tile getTile(Position position) {
        return tileMap.get(position);
    }

    /**
     * Get a set of ordered tiles from SparseTileArray in
     * breadth-first-search order. <br>
     * The startingTile (passed to addLinkTiles) should
     * be the first tile in the list. The following tiles
     * should be the tiles at the "north", "east", "south" and
     * "west" exits from the starting tile, if they exist. <br>
     * Then for each of those tiles, the next tiles will be their "north",
     * "east", "south" and "west" exits, if they exist.
     * The order should continue in the same way through all the tiles
     * that are linked to startingTile. <br>
     * The list returned by getTiles may be immutable, and
     * if not, changing the list (i.e., adding or removing elements)
     * should not change that returned by subsequent calls to
     * getTiles().
     * @return a list of tiles in breadth-first-search
     *         order.
     */
    public List<Tile> getTiles() {
        return new ArrayList<>(orderedTiles);
    }

    /**
     * Add a set of tiles to the sparse tilemap. <br>
     * This function does the following:
     * <ol>
     * <li> Remove any tiles that are already existing in the sparse map. </li>
     * <li> Add startingTile at position (startingX, startingY), such
     * that getTile(new Position(startingX, startingY)) == startingTile. </li>
     * <li> For each pair of linked tiles (tile1 at (x1, y1) and tile2 at
     * (x2, y2) that are accessible from startingTile (i.e. there is a
     * path through a series of exits
     * startingTile.getExits().get("north").getExits().get("east") ...
     * between the two tiles), tile2 will get a new position based on tile1's
     * position, and tile1's exit name.
     * <ul>
     *     <li> tile2 at "north"  exit should get a new position of
     *          (x1, y1 - 1) i.e. getTile(new Position(x1, y1 - 1))
     *          == tile1.getExits().get("north") </li>
     *     <li> tile2 at "east" exit should get a position of (x1 + 1, y1),
     *          i.e. getTile(new Position(x1 + 1, y1))
     *          == tile1.getExits().get("east") </li>
     *     <li> tile2 at "south" exit should get a position of (x1, y1 + 1),
     *          i.e. getTile(new Position(x1, y1 + 1))
     *          == tile1.getExits().get("south") </li>
     *     <li> tile2 at "west" exit should get a position of (x1 - 1, y1),
     *          i.e. getTile(new Position(x1 - 1, y1))
     *          == tile1.getExits().get("west")</li>
     * </ul>
     * </li>
     * <li> If there are tiles that are not geometrically consistent, i.e. Tiles
     * that would occupy the same position or require two different coordinates
     * for getTile() method to work, throw a WorldMapInconsistentException. <br>
     * Two examples of inconsistent tiles are:
     * <ol>
     *     <li> tile1.getExits().get("north").getExits().get("south) is non null
     *          and not == to tile1, throw a WorldMapInconsistentException.
     *          Note: one waY exits are allowed, so
     *          tile1.getExits().get("north").getExits().get("south) == null
     *          would be acceptable, but
     *          tile1.getExits().get("north").getExits().get("south)
     *          == tile2 foR some other non-null tile2 is not. </li>
     *     <li> tile1.getExits().get("north").getExits().get("north") == tile1.
     *          tile1 exits in two different places in this case. </li>
     * </ol>
     * </li>
     * <li> getTiles() should return a list of each accessible tile in a
     * breadth-first search order (see getTiles()) </li>
     *
     * <li> If an exception is thrown, reset the state of the SparseTileArray
     * such that getTile(new Position(x, y)) returns null for any x and y. </li>
     * </ol>
     *
     * @param startingTile the starting point in adding the linked tiles. All
     *                     added tiles must have a path (via multiple exits) to
     *                     this tile.
     * @param startingX    the x coordinate of startingTile in the array
     * @param startingY    the y coordinate of startingTile in the array
     * @throws WorldMapInconsistentException if the tiles in the set are not
     *                                       Geometrically consistent
     *
     * @require startingTile != null
     * @ensure tiles accessed through getTile() are geometrically consistent
     */
    public void addLinkedTiles(Tile startingTile, int startingX, int startingY)
            throws WorldMapInconsistentException {

        // reset the state of this SparseTileArray instance
        this.reset();

        Queue<Tile> tilesToProcess = new ArrayDeque<>();

        // lookup positions by tile
        // Note: tile equals/hashCode will be
        // default, so each tile instance will
        // be unique.
        HashMap<Tile, Position> tilePositions = new HashMap<>();

        Position startingPosition = new Position(startingX, startingY);

        // add the starting position to the queue for processing.
        addTileForProcessing(tilesToProcess, tileMap, tilePositions,
                startingPosition, startingTile);

        // constants for loop below
        final String[] EXITS = {"north", "east", "south", "west"};
        final int[] DIRECTIONS_X = {0, 1, 0, -1};
        final int[] DIRECTIONS_Y = {-1, 0, 1, 0};


        while (tilesToProcess.size() > 0) {
            // loop until there are no more tiles to process

            // get the next tile from the queue and its associated position
            Tile tile = tilesToProcess.element();
            orderedTiles.add(tile);

            Position position = tilePositions.get(tile);

            // remove the tile from the queue
            tilesToProcess.remove();

            for (int i = 0; i < EXITS.length; i++) {
                // go through each exit name ("north", "east", "south", "west"}

                // get the tile in that direction
                Tile tileInDirection = tile.getExits().get(EXITS[i]);

                // create the associated position in that direction
                Position positionInDirection =
                        new Position(position.getX() + DIRECTIONS_X[i],
                        position.getY() + DIRECTIONS_Y[i]);

                try {
                    if (checkExistingTileValid(tileMap, tilePositions,
                            positionInDirection, tileInDirection)) {

                        // if the tile is valid (hasn't already been placed, the map
                        // is still consistent) add the new tile for processing.
                        addTileForProcessing(tilesToProcess, tileMap, tilePositions,
                                positionInDirection, tileInDirection);
                    }
                } catch (WorldMapInconsistentException inconsistentException) {
                    reset();
                    throw inconsistentException;
                }
            }
        }
    }

    /**
     * Check to see whether we should add a tile a for processing. The following
     * cases are handled:
     * <ol>
     * <li> tile is null, in which case we return false</li>
     * <li> the tile has already been placed at a different position, so throw a
     * WorldMapInconsistentException </li>
     * <li> there is a different tile at the current position, so throw
     * a WorldMapInconsistentException </li>
     * <li> the tile has already been placed in a valid location,
     * so we return false (we don't want to place it again. </li>
     * </ol>
     *
     * @param positionToTile the current mapping from positions to tiles
     * @param tileToPosition the current mapping from tiles to positions
     * @param position       the position we want to place a tile at
     * @param tile           the tile we want to place
     * @return true if we can place tile at position, false otherwise.
     * @throws WorldMapInconsistentException
     */
    private static boolean checkExistingTileValid(Map<Position, Tile> positionToTile, Map<Tile, Position> tileToPosition,
                                                  Position position, Tile tile) throws WorldMapInconsistentException {
        if (tile == null) {
            // this exit is a dead end, do not go any further
            return false;
        }

        // get the tile at the new position, and position of
        // the new tile.
        Tile tileToTest = positionToTile.get(position);
        Position positionToTest = tileToPosition.get(tile);

        if (positionToTest != null && !(position.equals(positionToTest))) {
            // we have already placed this tile somewhere else
            // this is bad, it means the map is inconsistent.
            throw new WorldMapInconsistentException("Tile that should be at " + position +
                    " is already assigned a different position at " + positionToTest);
        }

        if (tileToTest != null && tile != tileToTest) {
            // if we get here, it means that a different
            // tile is present at the location where we
            // want to put our tile. This is bad.
            throw new WorldMapInconsistentException("Position " + position +
                    " is already occupied by a different tile.");
        }

        if (tileToTest == null) {
            // tileToTest and positionToTest should be null
            // iff the other is also null
            assert positionToTest == null;

            // there is nothing at the position
            // where we want to put our tile.
            // and the tile has not already been placed.
            return true;

        } else {

            // we have already correctly placed this tile
            // we do not want to place it again.
            return false;
        }


    }

    /**
     * Add a tile and position for processing. We had t
     * he tile to a queue of tiles to process,
     * and also to two maps, a mapping from positions
     * to tiles and a mapping from tiles to positions.
     *
     * @param tilesToProcess the queue of tiles to process further
     * @param positionToTile the current mapping from positions to tiles
     * @param tileToPosition the current mapping from tiles to positions
     * @param position       the position to add for processing
     * @param tile           the tile to add for processing
     */
    private static void addTileForProcessing(Queue<Tile> tilesToProcess,
                                             Map<Position, Tile> positionToTile,
                                             Map<Tile, Position> tileToPosition,
                                             Position position, Tile tile) {
        positionToTile.put(position, tile);
        tileToPosition.put(tile, position);
        tilesToProcess.add(tile);
    }

    /**
     * Reset the state of the SparseTileArray to default.
     */
    private void reset() {
        tileMap = new HashMap<>();
        orderedTiles = new ArrayList<>();
    }
}
