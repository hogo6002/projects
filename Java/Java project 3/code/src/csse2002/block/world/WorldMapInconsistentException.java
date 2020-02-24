package csse2002.block.world;

/**
 * A World Map file is geometrically inconsistent.
 * @serial exclude
 */
public class WorldMapInconsistentException extends BlockWorldException {

    public WorldMapInconsistentException() {
        super();
    }

    public WorldMapInconsistentException(String message) {
        super(message);
    }
}
