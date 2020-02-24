package csse2002.block.world;

/**
 * A wooden block or crate.
 * @serial exclude
 */
public class WoodBlock implements Block {
    /**
     * Get the colour of a WoodBlock. <br>
     * Always returns "brown".
     * @return "brown"
     */
    @Override
    public String getColour() {
        return "brown";
    }

    /**
     * Get the type of a WoodBlock. <br>
     * Always returns "wood".
     * @return "wood"
     */
    @Override
    public String getBlockType() {
        return "wood";
    }

    /**
     * A woodblock is diggable. <br>
     * Always returns true.
     * @return true
     */
    @Override
    public boolean isDiggable() {
        return true;
    }

    /**
     * A woodblock is carryable. <br>
     * Always returns true.
     * @return true
     */
    @Override
    public boolean isCarryable() {
        return true;
    }

    /**
     * A woodblock is moveable. <br>
     * Always returns true.
     * @return true
     */
    @Override
    public boolean isMoveable() {
        return true;
    }

}
