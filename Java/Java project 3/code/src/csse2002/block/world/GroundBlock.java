package csse2002.block.world;

/**
 * An abstract GroundBlock that enforces not moveable and diggable.
 * @serial exclude
 */
public abstract class GroundBlock implements Block {
    /**
     * Is the GroundBlock moveable?
     * GroundBlocks enforce not moving.
     * @return false
     */
    @Override
    public final boolean isMoveable() {
        return false;
    }

    /**
     * Is the GroundBlock diggable?
     * GroundBlocks enforce allowing digging.
     * @return true
     */
    @Override
    public final boolean isDiggable() {
        return true;
    }

}
