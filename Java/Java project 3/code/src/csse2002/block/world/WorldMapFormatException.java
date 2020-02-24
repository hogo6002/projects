package csse2002.block.world;

/**
 * A World Map file contains the wrong format.
 * @serial exclude
 */
public class WorldMapFormatException extends BlockWorldException {

    public WorldMapFormatException() {
        super();
    }

    public WorldMapFormatException(String message) {
        super(message);
    }
}
