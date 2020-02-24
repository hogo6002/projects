package pacman.ghost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import pacman.game.Entity;
import pacman.game.PacmanGame;
import pacman.util.Direction;
import pacman.util.Position;

/**
 * An Abstract Ghost which is a game entity.
 *
 * @ass1
 */
public abstract class Ghost extends Entity {

    // whether the ghost is dead
    private boolean dead;
    // current phase of this ghost
    private Phase phase;
    // duration of current phase
    private int phaseDuration;

    /**
     * Creates a ghost which is alive and starts in the SCATTER phase
     * with a duration of Phase.SCATTER.duration(). This ghost also has
     * a default position of (0, 0) and a default direction of facing
     * up.
     *
     * @ass1
     */
    public Ghost() {
        super();
        dead = false;
        phase = Phase.SCATTER;
        phaseDuration = Phase.SCATTER.getDuration();
    }

    /**
     * Sets the Ghost Phase and its duration overriding any current
     * phase information.
     *
     * if Phase is null then no changes are made. If the duration is
     * less than zero then the duration is set to 0.
     *
     * @param newPhase to set the ghost to.
     * @param duration of ticks for the phase to last for.
     * @ass1
     */
    public void setPhase(Phase newPhase, int duration) {
        if (newPhase != null) {
            phase = newPhase;
            phaseDuration = Integer.max(0, duration);
        }
    }

    /**
     * Get the phase that the ghost currently is in.
     * @return the set phase.
     * @ass1
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Gets the phase info of the ghost.
     * @return the phase and duration formatted as such: "PHASE:DURATION".
     * @ass1
     */
    public String phaseInfo() {
        return String.format("%s:%d", phase, phaseDuration);
    }

    /**
     * Gets the target block that we should be heading towards when in
     * the chase phase.
     * @param game to read the board from.
     * @return the ghosts target position.
     * @ass2
     */
    public abstract Position chaseTarget(PacmanGame game);

    /**
     * Gets the home block that we should be heading towards when in
     * the scatter phase.
     * @param game to read the board from.
     * @return the ghosts home position.
     * @ass2
     */
    public abstract Position home(PacmanGame game);

    /**
     * Gets the ghosts colour.
     * @return hex version of the ghosts colour, e.g. #FFFFFF for white.
     * @ass1
     */
    public abstract String getColour();

    /**
     * Gets the ghosts type.
     * @return this ghosts type.
     * @ass1
     */
    public abstract GhostType getType();

    /**
     * Move advances the ghost in a direction by one point on the board. The
     * direction this move is made is done as follows:
     *
     * <ol>
     *     <li>Decrease the phase duration by 1, and if the duration is now 
     *     zero, then move to the next phase.</li>
     *     <li>Get the target position. If the phase is CHASE, then get the
     *     chaseTarget. If the phase is SCATTER, then the position is the
     *     ghost's home position. If the phase is FRIGHTENED, then choose a 
     *     target position with coordinates given by:
     *     <br>targetPositionX = (x*24 mod (2 * board width )) - board width,
     *     <br>targetPositionY = (y*36 mod (2 * board height)) - board height
     *      <br> where x and y are the current coordinates of the ghost.  
     *     </li>
     *     <li>Choose the direction that the current Ghost position when moved
     *     1 step has the smallest euclidean distance to the target position. 
     *     The board item in the move position must be pathable for it 
     *     to be chosen. The chosen direction cannot be opposite to the current 
     *     direction. 
     *     If multiple directions have the same shortest distance,
     *     then choose the direction in the order UP, LEFT, DOWN, RIGHT</li>
     *     <li>Set the direction of the Ghost to the chosen direction. </li>
     *     <li>Set the position of this Ghost to be one forward step in 
     *     the chosen direction.</li>
     * </ol>
     *
     * <ul>
     *     <li>Note: The next phase after CHASE is SCATTER. </li>
     *     <li>Note: The next phase after FRIGHTENED or SCATTER is CHASE.</li>
     *     <li>Note: All positions outside the board are to be treated 
     *     as if they are not pathable. </li>
     * </ul>
     *
     * @param game information needed to decide movement.
     * @ass2
     */
    public void move(PacmanGame game) {
        nextPhase();

        Position target = target(game);
        
        // Go in order U, L, D, R
        var order = new ArrayList<>(List.of(
                Direction.UP, Direction.LEFT, 
                Direction.DOWN, Direction.RIGHT));
        
        // don't consider opposite direction
        order.remove(getDirection().opposite());
        
        // calc distances between each direction move and target.
        var distances = new HashMap<Direction, Double>();
        for (var direction : order) {
            var newPos = getPosition().add(direction.offset());
            
            if (game.getBoard().getEntry(newPos).getPathable()) {
                distances.put(direction, newPos.distance(target));
            }
        }

        // move in first direction with smallest distance to target
        double smallest = smallest(distances.values());
        for (var direction : order) {
            // check if this direction has smallest distance
            if (distances.containsKey(direction) 
                    && distances.get(direction) == smallest) {
                this.setPosition(getPosition().add(direction.offset()));
                this.setDirection(direction);
                break;
            }
        }
    }

    /*
     * NextPhase decreases our phase duration and moves us to the
     * next phase if it is 0.
     *
     * - CHASE goes to SCATTER.
     * - FRIGHTENED && SCATTER go to CHASE.
     */
    private void nextPhase() {
        phaseDuration = Integer.max(0, phaseDuration - 1);
        
        if (phaseDuration == 0) {
            switch (getPhase()) {
                case CHASE:
                    setPhase(Phase.SCATTER, Phase.SCATTER.getDuration());
                    break;
                case FRIGHTENED:
                case SCATTER:
                    setPhase(Phase.CHASE, Phase.CHASE.getDuration());
                    break;
            }
        }
    }

    /*
     * Chooses a position to target based on the ghost's current phase
     */
    private Position target(PacmanGame game) {
        switch (getPhase()) {
            default:
            case CHASE:
                return chaseTarget(game);
            case SCATTER:
                return home(game);
            case FRIGHTENED:
                int boardHeight = game.getBoard().getHeight();
                int boardWidth = game.getBoard().getWidth();
                
                int x = ((this.getPosition().getX() * 24) % (2 * boardWidth))
                        - boardWidth;
                int y = ((this.getPosition().getX() * 24) % (2 * boardHeight))
                        - boardHeight;
                
                return new Position(x, y);
        }
    }

    /*
     * Returns the smallest double value in given collection.
     */
    private double smallest(Collection<Double> values) {
        double smallest = Double.MAX_VALUE;

        for (var value : values) {
            if (value < smallest) {
                smallest = value;
            }
        }

        return smallest;
    }

    /**
     * Kills this ghost by setting its status to isDead.
     * @ass1
     */
    public void kill() {
        this.dead = true;
    }

    /**
     * Checks if this ghost is dead.
     * @return true if dead, false otherwise.
     * @ass1
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Resets the ghost back to an initial state where:
     *
     * <ul>
     *     <li>It is alive</li>
     *     <li>With a Phase of SCATTER with duration SCATTER.getDuration()</li>
     *     <li>Facing in the Direction.UP</li>
     *     <li>With a Position of ( 0, 0 )</li>
     * </ul>
     * @ass1
     */
    public void reset() {
        dead = false;
        this.phase = Phase.SCATTER;
        this.phaseDuration = Phase.SCATTER.getDuration();
        this.setDirection(Direction.UP);
        this.setPosition(new Position(0, 0));
    }

    /**
     * Checks if another object instance is equal to this Ghost. 
     * Ghosts are equal if they have the same alive/dead status, 
     * phase duration,current phase, direction and position.
     * @return true if equal, false otherwise.
     * @ass2
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!super.equals(o) || !(o instanceof Ghost)) {
            return false;
        }

        Ghost otherGhost = (Ghost) o;

        return dead == otherGhost.dead
                && phaseDuration == otherGhost.phaseDuration
                && phase == otherGhost.phase;
    }

    /**
     * For two objects that are equal the hash should also
     * be equal. For two objects that are not equal the hash
     * does not have to be different.
     * @return hash of Ghost.
     * @ass2
     */
    @Override
    public int hashCode() {
        return super.hashCode() + Boolean.hashCode(dead) + phase.hashCode()
                + (31 * phaseDuration);
    }

    /**
     * Represents this Ghost in a comma-separated string format.
     * Format is: "x,y,DIRECTION,PHASE:phaseDuration". 
     * DIRECTION is the uppercase enum type value for {@link Direction}. 
     * PHASE is the uppercase enum type value for {@link Phase}.
     * Example: "4,5,LEFT,FRIGHTENED:15"
     * @return "x,y,DIRECTION,PHASE:phaseDuration"
     * @ass2
     */
    @Override
    public String toString() {
        return super.toString() + "," + phaseInfo();
    }
}
