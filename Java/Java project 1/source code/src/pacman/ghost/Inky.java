package pacman.ghost;

import pacman.game.PacmanGame;
import pacman.util.Position;

/**
 * Inky is a ghost that likes to tail close behind the hunter.
 * When not chasing the hunter down, Inky likes to hang out in
 * the bottom right corner of the board in a blue glow.
 *
 * @ass1
 */
public class Inky extends Ghost {


    /**
     * Inky will chase 2 blocks behind the hunter's current direction.
     *
     * See: {@link PacmanGame#getHunter()}
     *
     * @param game to read positions from.
     * @return the position 2 blocks behind hunter position.
     * @ass2
     */
    @Override
    public Position chaseTarget(PacmanGame game) {
        var target = game.getHunter().getPosition();
        
        return target.add(
                game.getHunter().getDirection().offset().multiply(-2));
    }

    /**
     * Inky's home position is one block outside of the bottom right of
     * the game board. Where the top left position of the board is (0, 0).
     *
     * <p><b>Note:</b> this will be outside of the board.</p>
     *
     * @param game to read the board from.
     * @return One diagonal block out from the bottom right corner.
     * @ass2
     */
    @Override
    public Position home(PacmanGame game) {
        return new Position(game.getBoard().getWidth(),
                game.getBoard().getHeight());
    }

    /**
     * Get Inky's colour.
     * @return "#7aa6da"
     * @ass1
     */
    @Override
    public String getColour() {
        return "#7aa6da";
    }

    /**
     * Get Inky's type/name.
     * @return INKY;
     * @ass1
     */
    @Override
    public GhostType getType() {
        return GhostType.INKY;
    }
}
