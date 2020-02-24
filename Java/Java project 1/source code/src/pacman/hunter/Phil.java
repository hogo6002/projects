package pacman.hunter;

/**
 * A Phil hunter that has no special ability.
 * @ass1
 */
public class Phil extends Hunter {

    /**
     * Creates a Phil Hunter.
     *
     * see {@link Hunter#Hunter()}
     * @ass1
     */
    public Phil() {
        super();
    }

    /**
     * Creates a Phil hunter by copying the internal state of
     * another hunter.
     *
     * see {@link pacman.hunter.Hunter#Hunter(Hunter)}
     *
     * @param original hunter to copy from
     * @ass1
     */
    public Phil(Hunter original) {
        super(original);
    }

    /**
     * Phil does not have a special.
     * @return false
     * @ass1
     */
    @Override
    public boolean isSpecialActive() {
        return false;
    }

    /**
     * Represents this Phil in a comma-separated string format.
     * Format is: "x,y,DIRECTION,specialDuration,PHIL". 
     * DIRECTION is the uppercase enum type value. 
     * Example: "4,5,LEFT,12,PHIL"
     * @return "x,y,DIRECTION,specialDuration,PHIL"
     * @ass2
     */
    @Override
    public String toString() {
        return super.toString() + ",PHIL";
    }
}
