package pacman.game;

import pacman.util.Direction;
import pacman.util.Position;

/**
 * An object that can move with a current position and a direction.
 * @ass1
 */
public interface Moveable {

    /**
     * Sets the position of the entity, if position is null the
     * position is not set.
     * @param position to set to the Moveable.
     * @ass1
     */
    void setPosition(Position position);

    /**
     * Gets the current position of the Moveable
     * @return current position.
     * @ass1
     */
    Position getPosition();

    /**
     * Sets the direction of the entity, if direction is null the
     * direction is not set and remains the same.
     * @param direction to be set.
     * @ass1
     */
    void setDirection(Direction direction);

    /**
     * Gets the direction that this Moveable is facing.
     * @return the current direction of the Movable.
     * @ass1
     */
    Direction getDirection();

    /**
     * Moves the Movable object using information provided in the
     * PacmanGame.
     * @requires game != null.
     * @param game information needed to decide movement.
     * @ass2
     */
    void move(PacmanGame game);

}
