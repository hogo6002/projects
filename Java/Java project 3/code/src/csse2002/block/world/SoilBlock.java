package csse2002.block.world;

/**
 * A soil block.
 * @serial exclude
 */
public class SoilBlock extends GroundBlock {
    /**
     * Get the colour of a SoilBlock. <br>
     * Always returns "black".
     * @return "black"
     */
    @Override
    public String getColour() {
        return "black";
    }

    /**
     * Get the type of a SoilBlock. <br>
     * Always returns "soil".
     * @return "soil"
     */
    @Override
    public String getBlockType() {
        return "soil";
    }

    /**
     * SoilBlocks are carryable. <br>
     * Always returns true.
     * @return true
     */
    @Override
    public boolean isCarryable() {
        return true;
    }

}
