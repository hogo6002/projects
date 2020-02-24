package pacman.hunter;

import pacman.board.BoardItem;
import pacman.game.Entity;
import pacman.game.PacmanGame;
import pacman.ghost.Ghost;
import pacman.ghost.Phase;
import pacman.util.Direction;
import pacman.util.Position;

/**
 * Hunters are entities which are controlled by the player to clear
 * the board and win the game. Hunters in this version have special
 * abilities like "phasing through ghost's" and "run's two blocks at
 * a time" and so on. These special abilities are too add a bit of
 * variety into the game.
 *
 * <p>
 *     Note: during the first assignment the Hunter class has it's
 *     special abilities that have durations. In assignment two we
 *     will build upon the special ability and actually implement
 *     them. Currently in assignment 1 there is nothing that makes
 *     the duration of a special ability decrease ( this will be in
 *     Assignment 2 ).
 * </p>
 *
 * @ass1
 */
public abstract class Hunter extends Entity {

    /**
     * SPECIAL_DURATION has the default value of 20.
     * @ass2
     */
    public static final int SPECIAL_DURATION = 20;

    // The special ability duration.
    private int duration;
    // Whether the special has been used already.
    private boolean used;
    // Whether the Hunter is dead.
    private boolean dead;

    /**
     * Creates a Hunter setting the hunter to be alive with the
     * following conditions:
     *
     * The hunter has not used it's special yet.
     *
     * The hunter also does not have its special active.
     *
     * This hunter has a position of (0, 0) with a direction of UP.
     *
     * @ass1
     */
    public Hunter() {
        super();
        dead = false;
        duration = 0;
        used = false;
    }

    /**
     * Creates a Hunter where the following attributes are the same
     * between this hunter and the original:
     *
     * <ul>
     *     <li>Dead/Alive status</li>
     *     <li>Whether the hunter has used its special ability yet.</li>
     *     <li>The duration remaining of the special ability.</li>
     *     <li>The position and direction.</li>
     * </ul>
     *
     * @param original hunter to copy.
     * @ass1
     */
    public Hunter(Hunter original) {
        super();
        dead = original.dead;
        duration = original.duration;
        used = original.used;
        setPosition(original.getPosition());
        setDirection(original.getDirection());
    }

    /**
     * Tells if the hunter is dead.
     * @return true if dead, false otherwise.
     * @ass1
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Activates the hunter's special if the hunter hasn't already
     * used its special before. If the hunter has already used its
     * special then do not change the special duration.
     *
     * If the duration for the special is greater than zero then
     * use the hunter's special and set the special's duration to the
     * given duration.
     *
     * If the duration for the special is zero or lower than do not
     * change the special duration and do not use up the hunter's
     * special.
     *
     * @param duration to activate the special for.
     * @ass1
     */
    public void activateSpecial(int duration) {
        if (!used && duration > 0) {
            this.duration = duration;
            used = true;
        }
    }

    /**
     * Gets how many ticks of our special ability is remaining.
     * @return the amount of ticks remaining for the special.
     * @ass1
     */
    public int getSpecialDurationRemaining() {
        return duration;
    }

    /**
     * Checks if the special is currently active.
     * @return true if the special ability has a duration remaining
     * that is greater than 0 ticks.
     * @ass1
     */
    public boolean isSpecialActive() {
        return this.duration > 0;
    }

    /**
     * Checks to see if the hunter is at the same position of the
     * ghost. If the ghost and hunter do have the same position then
     * if the ghost is Phase.FRIGHTENED the ghost is killed
     * {@link Ghost#kill()} otherwise the ghost kills the hunter.
     *
     * If the ghost and hunter are not at the same position then
     * do nothing.
     *
     * @param ghost to check if were colliding with.
     * @throws NullPointerException is ghost is null.
     * @ass1
     */
    public void hit(Ghost ghost) throws NullPointerException {
        if (ghost == null) {
            throw new NullPointerException();
        }
        
        if (getPosition().equals(ghost.getPosition())) {
            if (ghost.getPhase() == Phase.FRIGHTENED) {
                ghost.kill();
            } else {
                this.dead = true;
            }
        }
    }

    /**
     * Resets this hunter to be:
     *
     * <ul>
     *     <li>Alive</li>
     *     <li>With a special that has not been used yet</li>
     *     <li>A special that is not active ( duration of 0 )</li>
     *     <li>With a Direction of Direction.UP</li>
     *     <li>With a Position of ( 0, 0 )</li>
     * </ul>
     * @ass1
     */
    public void reset() {
        dead = false;
        used = false;
        duration = 0;
        setDirection(Direction.UP);
        setPosition(new Position(0, 0));
    }

    /**
     * Moves the Hunter across the board.
     *
     * <p>If the BoardItem one position forward in the hunter's current 
     * direction is pathable, move the hunter into this position. 
     * Otherwise the hunter stays in its current position.</p>
     *
     * <p>After moving, the hunter will eat the item that occupied the
     * block and will add its score to the game score.</p>
     *
     * <p>Lastly the special duration will be decreased by 1 if it
     * is greater than 0.</p>
     *
     * Note: The hunter cannot move off of the board, any position outside
     * the board is not pathable.
     *
     * @param game information needed to decide movement.
     * @ass2
     */
    @Override
    public void move(PacmanGame game) {
        var nextPos = this.getPosition().add(this.getDirection().offset());
        var block = game.getBoard().eatDot(nextPos);

        if (block.getPathable()) {
            this.setPosition(nextPos);
        }

        game.getScores().increaseScore(block.getScore());

        if (block == BoardItem.BIG_DOT) {
            game.setGhostsFrightened();
        }

        duration = Integer.max(0, duration - 1);
    }

    /**
     * Checks if another object instance is equal to this. 
     * Hunters are equal if they have the same:
     * <ul>
     * <li>Alive/dead value.</li>
     * <li>Special duration.</li>
     * <li>Special used status.</li>
     * <li>Direction.</li>
     * <li>Position.</li>
     * </ul>
     * @return true if same, false otherwise.
     * @ass2
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (!super.equals(o) || !(o instanceof Hunter)) {
            return false;
        }

        Hunter otherHunter = (Hunter) o;

        return duration == otherHunter.duration
                && used == otherHunter.used
                && dead == otherHunter.dead;
    }

    /**
     * For two objects that are equal the hash should also
     * be equal. For two objects that are not equal the hash
     * does not have to be different.
     * @return hash of the Hunter.
     * @ass2
     */
    @Override
    public int hashCode() {
        return super.hashCode() + 31 * duration 
                + Boolean.hashCode(used) + Boolean.hashCode(dead);
    }

    /**
     * Represents this Hunter in a comma-seperated string format. 
     * Format is: "x,y,DIRECTION,specialDuration". 
     * DIRECTION is the uppercase enum type value. 
     * Example: "4,5,LEFT,12"
     * @return "x,y,DIRECTION,specialDuration"
     * @ass2
     */
    @Override
    public String toString() {
        return super.toString() + "," + duration;
    }
}
