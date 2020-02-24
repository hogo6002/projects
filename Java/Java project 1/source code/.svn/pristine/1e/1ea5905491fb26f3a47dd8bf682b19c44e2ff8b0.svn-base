package pacman.ghost;

import pacman.game.PacmanGame;
import pacman.util.Position;

/**
 * Pinky is a cunning ghost that tries to ambush the hunter.
 * When not chasing the hunter down, Pinky likes to hang out in
 * the top left corner of the board in a pink glow.
 *
 * @ass1
 */
public class Pinky extends Ghost {

    /**
     * Pinky will chase 4 blocks in front of the hunter's current direction.
     * @param game to read positions from.
     * @return the position 4 blocks in front of hunter position.
     * @ass2
     */
    @Override
    public Position chaseTarget(PacmanGame game) {
        var target = game.getHunter().getPosition();
        
        return target.add(
                game.getHunter().getDirection().offset().multiply(4));
    }

    /**
     * Pinky's home position is one block outside of the top left of
     * the game board. Where the top left position of the board is (0, 0).
     *
     * <p><b>Note:</b> this will be outside of the board.</p>
     *
     * @param game to read the board from.
     * @return One diagional block out from the top left corner.
     * @ass2
     */
    @Override
    public Position home(PacmanGame game) {
        return new Position(-1, -1);
    }

    /**
     * Get Pinky's colour.
     * @return "#c397d8"
     * @ass1
     */
    @Override
    public String getColour() {
        return "#c397d8";
    }

    /**
     * Get Pinky's type/name.
     * @return PINKY;
     * @ass1
     */
    @Override
    public GhostType getType() {
        return GhostType.PINKY;
    }
}
