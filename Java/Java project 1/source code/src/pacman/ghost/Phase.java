package pacman.ghost;

/**
 * Phase
 *
 * Defines the different phases a ghost can be in. The phases are
 * defined as "CHASE", "SCATTER" and "FRIGHTENED".
 * <ul>
 *     <li>Chase has a duration of 20.</li>
 *     <li>Scatter has a duration of 10.</li>
 *     <li>Frightened has a duration of 30.</li>
 * </ul>
 *
 * @ass1
 */
public enum Phase {
    /**
     * Phase where the ghosts chase the hunter.
     */
    CHASE(20),
    /**
     * Phase where the ghosts run home.
     */
    SCATTER(10),
    /**
     * Phase where the ghosts are frightened and confused.
     */
    FRIGHTENED(30);

    // duration of a phase
    private int duration;

    /**
     * Creates a phase with the specified duration.
     * @param duration for the phase.
     * @ass1
     */
    Phase(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the duration of the phase.
     * @return duration of the phase.
     * @ass1
     */
    public int getDuration() {
        return this.duration;
    }
}
