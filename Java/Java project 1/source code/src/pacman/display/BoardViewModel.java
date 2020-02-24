package pacman.display;

import javafx.util.Pair;
import pacman.board.PacmanBoard;
import pacman.game.PacmanGame;
import pacman.ghost.Ghost;
import pacman.ghost.Phase;
import pacman.util.Position;

import java.util.LinkedList;
import java.util.List;

public class BoardViewModel {
    PacmanGame pacmanGame;

    public BoardViewModel(PacmanGame model) {
        pacmanGame = model;
    }

    public int getLives() {
        return pacmanGame.getLives();
    }

    public int getLevel() {
        return pacmanGame.getLevel();
    }

    public String getPacmanColour() {
        if (pacmanGame.getHunter().isSpecialActive()) {
            return "#CDC3FF";
        }
        return "#FFE709";
    }

    public int getPacmanMouthAngle() {
        switch (pacmanGame.getHunter().getDirection()) {
            case RIGHT:
                return 30;
            case UP:
                return 120;
            case LEFT:
                return 210;
            default:
                return 300;
        }
    }

    public Position getPacmanPosition() {
        return pacmanGame.getHunter().getPosition();
    }

    public PacmanBoard getBoard() {
        return pacmanGame.getBoard();
    }

    public List<Pair<Position, String>> getGhosts() {
        List<Pair<Position, String>> ghostsList = new LinkedList<>();
        for (Ghost ghost : pacmanGame.getGhosts()) {
            // set colour to be "#0000FF" if ghost's phase is FRIGHTENED,
            // otherwise, the colour should be itself color.
            ghostsList.add(new Pair<>(ghost.getPosition(),
                            ghost.getPhase().equals(Phase.FRIGHTENED) ?
                                    "#0000FF" : ghost.getColour()));
        }
        return ghostsList;
    }
}
