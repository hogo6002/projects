package pacman.hunter;

import pacman.game.PacmanGame;

/**
 * A Speedy hunter that has a special ability that allows the hunter
 * to travel twice as fast.
 *
 * @ass1
 */
public class Speedy extends Hunter {

    /**
     * Creates a Speedy Hunter with its special ability.
     *
     * see {@link Hunter#Hunter()}
     * @ass1
     */
    public Speedy() {
        super();
    }

    /**
     * Creates a Speedy Hunter by copying the internal state of
     * another hunter.
     *
     * see {@link pacman.hunter.Hunter#Hunter(Hunter)}
     *
     * @param original hunter to copy from
     * @ass1
     */
    public Speedy(Hunter original) {
        super(original);
    }

    /**
     * If Speedy's special is active then we move twice instead of once.
     * While moving we still do all the normal steps that
     * {@link pacman.hunter.Hunter#move(PacmanGame)} does.
     *
     * <p>This will result in Speedy decreasing its special duration by
     * two.</p>
     *
     * @param game that stores the internal state.
     * @ass2
     */
    @Override
    public void move(PacmanGame game) {
        if (isSpecialActive()) {
            super.move(game);
        }
        
        super.move(game);
    }

    /**
     * Represents this Speedy in a comma-separated string format.
     * Format is: "x,y,DIRECTION,specialDuration,SPEEDY". 
     * DIRECTION is the uppercase enum type value. 
     * Example: "4,5,LEFT,12,SPEEDY"
     * @return "x,y,DIRECTION,specialDuration,SPEEDY"
     * @ass2
     */
    @Override
    public String toString() {
        return super.toString() + ",SPEEDY";
    }
}
