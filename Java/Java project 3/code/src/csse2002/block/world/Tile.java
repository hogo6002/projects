package csse2002.block.world;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



/**
 * Tiles for a map. <br>
 * Contains {@link Block Block}s <br>
 * Maintains a mapping between exit names and other tiles. <br>
 * @serial exclude
 */
public class Tile implements Serializable {
    /* The maximum number of blocks allowed on a tile. */
    private static final int MAX_BLOCKS = 8;

    /* The maximum number of ground allowed on a tile. */
    private static final int MAX_GROUND_BLOCKS = 3;

    /* Exits from this Tile. Strings are names of the exits */
    private Map<String, Tile> exits;

    /* Blocks in this Tile*/
    private List<Block> blocks;

    /**
     * Construct a new tile.<br>
     * Each tile should be constructed with no exits (getExits().size() == 0).
     *<br>
     * Each tile must be constructed to start with two soil blocks and then
     * a grass block on top.<br>
     * i.e. getBlocks() must contain {SoilBlock, SoilBlock, GrassBlock} for
     * a new Tile.
     */
    public Tile() {
        exits = new TreeMap<String, Tile>();

        // use a list for now, but could be a stack
        blocks = new LinkedList<Block>();

        // each tile starts with 2 soil blocks and 1 grass block
        blocks.add(new SoilBlock());
        blocks.add(new SoilBlock());
        blocks.add(new GrassBlock());
    }

    /**
     * Construct a new tile.<br>
     * Each tile should be constructed with no exits (getExits().size() == 0).
     *<br>
     * Set the blocks on the tile to be the contents of startingBlocks. <br>
     * Index 0 in startingBlocks is the lowest block on the tile, while index
     * N -1 is the top block on the tile for N blocks. <br>
     * startingBlocks cannot be null. <br>
     * i.e. getBlocks() must contain the contents of startingBlocks, but
     * modifying startingBlocks after constructing the Tile should not
     * change the results of getBlocks(). <br>
     * Handle the following cases:
     * <ol>
     * <li> If startingBlocks contains more than 8 elements, throw a
     *TooHighException. </li>
     * <li> If startingBlocks contains an instance of GroundBlock that is at an
     *index
     * of 3 or higher, throw a TooHighException. </li>
     * </ol>
     * @param startingBlocks a list of blocks on the tile, cannot be null
     * @throws TooHighException if startingBlocks.size() &gt; 8, or if
     *                          startingBlocks elements with index &ge; 3
     *                          are instances of GroundBlock
     */
    public Tile(List<Block> startingBlocks) throws TooHighException {
        exits = new TreeMap<>();

        if (startingBlocks.size() > 8) {
            throw new TooHighException();
        }

        // check for ground blocks that are too high
        for (int i = MAX_GROUND_BLOCKS; i < startingBlocks.size(); i++) {
            if (startingBlocks.get(i) instanceof GroundBlock) {
                throw new TooHighException();
            }
        }

        // make a copy of startingBlocks
        blocks = new LinkedList<>(startingBlocks);
    }

    /**
     * What exits are there from this Tile? <br>
     * No ordering is required.
     * @return map of names to Tiles
     */
    public Map<String, Tile> getExits() {
        return Collections.unmodifiableMap(this.exits);
    }

    /**
     * What Blocks are on this Tile? <br>
     * Order of blocks returned must be in order of height. <br>
     * Index 0 is bottom, and index N - 1 is the top, for N blocks.
     * @return Blocks on the Tile
     */
    public List<Block> getBlocks() {
        return Collections.unmodifiableList(this.blocks);
    }

    /**
     * Return the block that is the top block on the tile. <br>
     * If there are no blocks, throw a TooLowException
     * @return the top Block
     * @throws TooLowException if there are no blocks on the tile
     */
    public Block getTopBlock() throws TooLowException {
        if (blocks.size() == 0) {
            throw new TooLowException();
        }

        return blocks.get(blocks.size() - 1);
    }

    /**
     * Remove the block on top of the tile. <br>
     * Throw a TooLowException if there are no blocks on the tile.
     * @throws TooLowException if there are no blocks on the tile
     */
    public void removeTopBlock() throws TooLowException {
        if (blocks.size() == 0) {
            throw new TooLowException();
        }

        blocks.remove(blocks.size() - 1);
    }

    /**
     * Add a new exit to this tile. <br>
     * The Map returned by getExits() must now include an
     * entry (name, target).
     * Overwrites any existing exit with the same name <br>
     * If name or target is null, throw a NoExitException.
     * @param name Name of the exit
     * @param target Tile the exit goes to
     * @throws NoExitException if name or target is null
     */
    public void addExit(String name, Tile target) throws NoExitException {
        if (name == null || target == null) {
            throw new NoExitException();
        }

        // add to exits
        exits.put(name, target);
    }

    /**
     * Remove an exit from this tile <br>
     * The Map returned by getExits() must no longer have
     * the key name. <br>
     * If name does not exist in getExits(), or name is null,
     * throw a NoExitException.
     * @param name Name of exit to remove
     * @throws NoExitException if name is not in exits, or name is null
     */
    public void removeExit(String name) throws NoExitException {
        if (name == null || exits.containsKey(name) == false) {
            throw new NoExitException();
        }

        exits.remove(name);
    }

    /**
     *  Attempt to dig in the current tile. <br>
     * If the top block (given by getTopBlock()) is diggable
     *(block.isDiggable()), remove the top block of the
     * tile and return it. <br>
     * Handle the following cases:
     * <ol>
     * <li> Throw a TooLowException if there are no blocks on the tile </li>
     * <li> Throw an InvalidBlockException if the block is not diggable </li>
     * </ol>
     * @return the removed block
     * @throws TooLowException if there are no blocks on the tile
     * @throws InvalidBlockException if the block is not diggable
     */
    public Block dig() throws TooLowException, InvalidBlockException {

        if (blocks.size() == 0) {
            throw new TooLowException();
        }

        Block result = blocks.get(blocks.size() - 1);

        if (!result.isDiggable()) {
            throw new InvalidBlockException();
        }

        removeTopBlock();
        return result;
    }

    /**
     * Attempt to move the current top block to
     * another tile.
     * Remove the top block (given by getTopBlock()) from this tile and add it
     * to the tile at the named exit (exitName in getExits()), if the block is
     *moveable (block.isMoveable())
     * and the height of that tile (the number of blocks given by
     *getBlocks().size()) is
     * less than the current tile *before* the move. <br>
     * Handle the following cases:
     * <ul>
     * <li> If the exit is null, or does not exist, throw a NoExitException
     *</li>
     * <li> If the number of blocks on the target tile is &ge; to this one,
     *throw a TooHighException </li>
     * <li> If the block is not moveable, throw a InvalidBlockException </li>
     * </ul>
     * @param exitName the name of the exit to move the block to
     * @throws TooHighException if the target tile is &ge; to this one.
     * @throws InvalidBlockException if the block is not moveable
     * @throws NoExitException if the exit is null or does not exist
     */
    public void moveBlock(String exitName) throws TooHighException,
            InvalidBlockException, NoExitException {
        if (exitName == null || !exits.containsKey(exitName)) {
            throw new NoExitException();
        }

        Tile exit = exits.get(exitName);
        if (exit.getBlocks().size() >= blocks.size()) {
            throw new TooHighException();
        }

        Block block = null;
        try {
            block = getTopBlock();
        } catch (TooLowException tooLow) {
            // not possible to end up here,
            // but required anyway.
            assert (false);
        }

        if (!block.isMoveable()) {
            throw new InvalidBlockException();
        }

        // should not throw TooHighException, because must be < 8 blocks
        exit.placeBlock(block);

        try {
            removeTopBlock();
        } catch (TooLowException tooLow) {
            // not possible to end up here,
            // but required anyway.
            assert (false);
        }
    }

    /**
     * Place a block on a tile.
     * Add the block to the top of the blocks on this tile.
     * If the block is an instance of GroundBlock, it can
     * only be placed underground.
     * Handle the following cases:
     * <ul>
     * <li> If the block is null, throw an InvalidBlockException </li>
     * <li> If the target tile has 8 blocks already, or if the block is
     * a GroundBlock and the target tile has 3 or more blocks already, throw
     * a TooHighException </li>
     * </ul>
     * @param block the block to place.
     * @throws TooHighException if there are already 8 blocks on the tile, or
     *                          if this is a ground block and there are already
     *                          3 or more blocks on the tile.
     * @throws InvalidBlockException if the block is null
     */
    public void placeBlock(Block block) throws TooHighException,
            InvalidBlockException {
        if (block == null) {
            throw new InvalidBlockException();
        }

        if (blocks.size() >= MAX_BLOCKS
                || (block instanceof GroundBlock
                && blocks.size() >= MAX_GROUND_BLOCKS)) {
            throw new TooHighException();
        }

        blocks.add(block);
    }

}
