package csse2002.block.world;

/**
 * A grass block.
 * @serial exclude
 */
public class GrassBlock extends GroundBlock {

    /**
     * Get the colour of a GrassBlock. <br>
     * Always returns "green"
     * @return "green"
     */
    @Override
    public String getColour() {
        return "green";
    }

    /**
     * Get the type of a GrassBlock. <br>
     * Always returns "grass"
     * @return "grass"
     */
    @Override
    public String getBlockType() {
        return "grass";
    }

    /**
     * GrassBlocks are not carryable. <br>
     * Always returns false
     * @return false
     */
    @Override
    public boolean isCarryable() {
        return false;
    }

}
