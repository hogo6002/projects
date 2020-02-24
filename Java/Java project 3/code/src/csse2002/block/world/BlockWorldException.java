package csse2002.block.world;

/**
 * Base class for custom exceptions related to blocks.
 * @serial exclude
 */
public class BlockWorldException extends Exception {
    public BlockWorldException() {

    }

    public BlockWorldException(String message) {
        super(message);
    }

}
