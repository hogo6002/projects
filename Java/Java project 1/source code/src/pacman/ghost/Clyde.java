package pacman.ghost;

import pacman.game.PacmanGame;
import pacman.util.Position;

/**
 * Clyde is a ghost that behaves in a very scared manner
 * when close to a hunter. When not chasing the hunter down, clyde
 * likes to hang out in the bottom left corner of the board in a
 * orange glow.
 *
 * @ass1
 */
public class Clyde extends Ghost {

    /**
     * Clyde will target the hunter if equal to or greater than a
     * distance of 8 away from the hunter. Otherwise if closer than 8
     * it will target its home position. See: {@link Clyde#home(PacmanGame)}
     * 
     * See: {@link PacmanGame#getHunter()}
     *
     * @param game to read positions from.
     * @return home if less than 8 away from hunter, otherwise hunter position.
     * @ass2
     */
    @Override
    public Position chaseTarget(PacmanGame game) {
        Position target = game.getHunter().getPosition();
        
        if (target.distance(getPosition()) < 8) {
            target = home(game);
        }
        
        return target;
    }

    /**
     * Clyde's home position is one block outside of the bottom left of
     * the game board. Where the top left position of the board is (0, 0).
     *
     * <p><b>Note:</b> this will be outside of the board.</p>
     *
     * @param game to read the board from.
     * @return One diagional block out from the bottom left corner.
     * @ass2
     */
    @Override
    public Position home(PacmanGame game) {
        return new Position(-1, game.getBoard().getHeight());
    }

    /**
     * Get Clyde's colour.
     * @return "#e78c45"
     * @ass1
     */
    @Override
    public String getColour() {
        return "#e78c45";
    }

    /**
     * Get Clyde's type/name.
     * @return CLYDE;
     * @ass1
     */
    @Override
    public GhostType getType() {
        return GhostType.CLYDE;
    }
}
