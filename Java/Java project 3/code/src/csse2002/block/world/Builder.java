package csse2002.block.world;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A Player who modifies the map. <br>
 * Manages an inventory of {@link Block Block}s <br>
 * Maintains a position in the map (by maintaining the current tile
 * that the Builder is on)
 * @serial exclude
 */
public class Builder {
    /* Our inventory */
    private List<Block> contents;

    /* Where the builder currently is */
    private Tile currentTile;

    /* Name of the builder*/
    String name;

    /**
     * Create a builder. <br>
     * Set the name of the Builder (such that getName() == name) and the
     * current tile to
     * startingTile (such that getCurrentTile() == startingTile).
     * @param name name of the builder (returned by getName())- cannot be null
     * @param startingTile the tile the builder starts in - cannot be null
     */
    public Builder(String name, Tile startingTile) {
        this.name = name;
        currentTile = startingTile;
        contents = new LinkedList<Block>();
    }

    /**
     * Create a builder <br>
     * Set the name of the Builder (such that getName() == name) and the
     * current tile to
     * startingTile (such that getCurrentTile() == startingTile). <br>
     * Copy the starting inventory into the builder's inventory, such that the
     * contents of getInventory() are identical to startingInventory. <br>
     * i.e. getInventory() must contain the contents of startingInventory, but
     * modifying startingInventory after the Builder is constructed should not
     * change the result of getInventory().
     * @param name name of the builder (returned by getName()) - cannot be null
     * @param startingTile the tile the builder starts in - cannot be null
     * @param startingInventory the starting inventory (blocks) - cannot be
     *                          null
     * @throws InvalidBlockException if for any Block (block) in
     *                               startingInventory,
     *                               block.isCarryable() == false
     */
    public Builder(String name, Tile startingTile,
            List<Block> startingInventory) throws InvalidBlockException {
        this.name = name;
        currentTile = startingTile;
        contents = new LinkedList<Block>();

        // copy starting inventory into contents
        for (Block block: startingInventory) {
            if (!block.isCarryable()) {
                throw new InvalidBlockException();
            }

            contents.add(block);
        }
    }

    /**
     * Get the Builder's name.
     * @return the Builder's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current tile that the builder is on.
     * @return the current tile
     */
    public Tile getCurrentTile() {
        return currentTile;
    }

    /**
     * What is in the Builder's inventory.
     * @return blocks in the inventory
     */
    public List<Block> getInventory() {
        return Collections.unmodifiableList(this.contents);
    }

    /**
     * Drop a block from inventory on the top of the current tile <br>
     * The block at inventoryIndex should be removed from the Builder's
     * inventory, and added to the Builder's current tile. <br>
     * Blocks can only be dropped on tiles with less than 8 blocks,
     * or tiles with less than 3 blocks if a GroundBlock. <br>
     * Note: the current tile is that given by getCurrentTile() and the index
     * should
     * refer to an item in the list returned by getInventory() <br>
     * Handle the following cases:
     * <ol>
     * <li> If the inventoryIndex is &lt; 0 or &ge;
     * the inventory size, throw an InvalidBlockException. </li>
     * <li> If there are 8 blocks on the current tile, throw a
     * TooHighException. </li>
     * <li> If there are 3 or more blocks on the current tile, and the
     * inventory block is a GroundBlock, throw a TooHighException
     * </ol>
     * Hint: call Tile.placeBlock, after checking the inventory
     * @param inventoryIndex the index in the inventory to place
     * @throws InvalidBlockException if the inventoryIndex is out of the
     *                               inventory range
     * @throws TooHighException if there are 8 blocks on the current tile
     *                          already, or if the block is an instance of
     *                          GroundBlock and there are already 3 or more
     *                          blocks on the current tile.
     */
    public void dropFromInventory(int inventoryIndex) throws
            InvalidBlockException, TooHighException {
        if (inventoryIndex < 0 || inventoryIndex >= contents.size()) {
            throw new InvalidBlockException();
        }

        Block block = contents.get(inventoryIndex);

        // should handle the TooHighException
        currentTile.placeBlock(block);

        contents.remove(inventoryIndex);
    }

    /**
     * Attempt to dig in the current tile and add tile to the inventory <br>
     * If the top block (given by getCurrentTile().getTopBlock()) is diggable,
     * remove the top block of the
     * tile and destroy it, or add it to the end of the inventory (given by
     * getInventory()). <br>
     * Handle the following cases:
     * <ol>
     * <li> If there are no blocks on the current tile, throw a TooLowException
     *</li>
     * <li> If the top block is not diggable, throw a InvalidBlockException
     *</li>
     * <li> If the top block is not carryable, remove the block, but do not
     * add it to the inventory. </li>
     * </ol>
     * Hint: call Tile.dig()
     * @throws TooLowException if there are no blocks on the current tile.
     * @throws InvalidBlockException if the top block is not diggable
     */
    public void digOnCurrentTile() throws TooLowException,
            InvalidBlockException {

        // throws TooLowException if no blocks, and InvalidBlockException if
        // not diggable.
        Block block = currentTile.dig();

        // only add the block to the inventory if it is carryable.
        if (block.isCarryable()) {
            contents.add(block);
        }
    }

    /**
     * Check if the Builder can enter a tile from the current tile. <br>
     * Returns true if:
     * <ol>
     * <li> the tiles are connected via an exit (i.e. there
     * is an exit from the current tile to the new tile), and </li>
     * <li> the height of the new tile (number of blocks) is the same
     * or different by 1 from the current tile (i.e.
     * abs(current tile height - new tile) &lt;= 1) </li>
     * </ol>
     * If newTile is null return false.
     * @param newTile the tile to test if we can enter
     * @return true if the tile can be entered
     */
    public boolean canEnter(Tile newTile) {

        if (newTile == null) {
            return false;
        }

        boolean tilesAreConnected = false;
        boolean heightsAreCompatible = false;

        for (Map.Entry<String,
                       Tile> entry : currentTile.getExits().entrySet()) {
            if (entry.getValue() == newTile) {
                tilesAreConnected = true;
                break;
            }
        }

        if (Math.abs(newTile.getBlocks().size()
                     - currentTile.getBlocks().size()) <= 1) {
            heightsAreCompatible = true;
        }


        return tilesAreConnected && heightsAreCompatible;
    }

    /**
     * Move the builder to a new tile. <br>
     * If canEnter(newTile) == true then
     * change the builders current tile
     * to be newTile. (i.e. getCurrentTile() == newTile) <br>
     * If canEnter(newTile) == false then
     * throw a NoExitException.
     * @param newTile the tile to move to
     * @throws NoExitException if canEnter(newTile) == false
     */
    public void moveTo(Tile newTile) throws NoExitException {
        if (!canEnter(newTile)) {
            throw new NoExitException();
        }

        currentTile = newTile;
    }

}
