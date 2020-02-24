package pacman.hunter;

import pacman.ghost.Ghost;
import pacman.ghost.Phase;

/**
 * A Phasey hunter with a special ability that allows the hunter
 * to travel through ghosts temporarily without dieing.
 * @ass1
 */
public class Phasey extends Hunter {

    /**
     * Creates a Phasey Hunter with its special ability.
     *
     * see {@link Hunter#Hunter()}
     * @ass1
     */
    public Phasey() {
        super();
    }

    /**
     * Creates a Phasey Hunter by copying the internal state of
     * another hunter.
     *
     * see {@link pacman.hunter.Hunter#Hunter(Hunter)}
     *
     * @param original hunter to copy from
     * @ass1
     */
    public Phasey(Hunter original) {
        super(original);
    }

    /**
     * If Phasey's special is active and if a ghost is not Phase.FRIGHTENED
     * then we travel through the ghost without killing them or them
     * killing us. Otherwise we behave as a normal Hunter.
     *
     * see {@link pacman.hunter.Hunter#hit(Ghost)}
     *
     * @param ghost to check if we are colliding with.
     * @ass1
     */
    @Override
    public void hit(Ghost ghost) {
        if (getPosition().equals(ghost.getPosition())) {
            if (ghost.getPhase() == Phase.FRIGHTENED) {
                ghost.kill();
            } else if (!isSpecialActive()) {
                super.hit(ghost);
            }
        }
    }

    /**
     * Represents this Phasey in a comma-separated string format.
     * Format is: "x,y,DIRECTION,specialDuration,PHASEY". 
     * DIRECTION is the uppercase enum type value. 
     * Example: "4,5,LEFT,12,PHASEY"
     * @return "x,y,DIRECTION,specialDuration,PHASEY"
     * @ass2
     */
    @Override
    public String toString() {
        return super.toString() + ",PHASEY";
    }
}
