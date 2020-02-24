package pacman.display;

import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pacman.board.PacmanBoard;
import pacman.game.PacmanGame;
import pacman.ghost.Ghost;
import pacman.ghost.Phase;
import pacman.hunter.Hungry;
import pacman.hunter.Hunter;
import pacman.util.Direction;
import pacman.util.Position;


import static org.junit.Assert.*;

public class BoardViewModelTest {
    private PacmanGame pacmanGame;

    @Before
    public void setUp() throws Exception {
        Hunter hunter = new Hungry();
        pacmanGame = new PacmanGame("title", "Diane", hunter,
                new PacmanBoard(5, 6));
    }

    @After
    public void tearDown() throws Exception {
        pacmanGame = null;
    }

    @Test
    public void constructorTest() {
        pacmanGame.setLevel(4);
        pacmanGame.setLives(2);
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        assertEquals(2, boardViewModel.getLives());
        assertEquals(4, boardViewModel.getLevel());
        assertEquals(pacmanGame.getBoard(), boardViewModel.getBoard());

    }

    /**
     * test the normal situation
     */
    @Test
    public void getLivesTest1() {
        pacmanGame.setLives(2);
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        assertEquals(2, boardViewModel.getLives());
    }

    /**
     * boundary test, when live is a large number
     */
    @Test
    public void getLivesTest2() {
        pacmanGame.setLives(100000);
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        assertEquals(100000, boardViewModel.getLives());
    }

    /**
     * boundary test, when live is 0
     */
    @Test
    public void getLivesTest3() {
        pacmanGame.setLives(0);
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        assertEquals(0, boardViewModel.getLives());
    }


    @Test
    public void getLevel() {
        pacmanGame.setLevel(4);
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        assertEquals(4, boardViewModel.getLevel());
    }

    // Test when hunter's special is not active
    @Test
    public void getPacmanColourTest1() {
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        assertEquals("#FFE709", boardViewModel.getPacmanColour());
    }

    // Test when hunter's special is active
    @Test
    public void getPacmanColourTest2() {
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        pacmanGame.getHunter().activateSpecial(20);
        assertEquals("#CDC3FF", boardViewModel.getPacmanColour());
    }

    // Test four direction for hunter
    @Test
    public void getPacmanMouthAngle() {
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        pacmanGame.getHunter().setDirection(Direction.RIGHT);
        assertEquals(30, boardViewModel.getPacmanMouthAngle());
        pacmanGame.getHunter().setDirection(Direction.UP);
        assertEquals(120, boardViewModel.getPacmanMouthAngle());
        pacmanGame.getHunter().setDirection(Direction.LEFT);
        assertEquals(210, boardViewModel.getPacmanMouthAngle());
        pacmanGame.getHunter().setDirection(Direction.DOWN);
        assertEquals(300, boardViewModel.getPacmanMouthAngle());
    }

    @Test
    public void getPacmanPosition() {
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        assertEquals(pacmanGame.getHunter().getPosition(),
                boardViewModel.getPacmanPosition());
    }

    @Test
    public void getBoard() {
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        assertEquals(pacmanGame.getBoard(), boardViewModel.getBoard());
    }

    // test when ghost's phase is not FRIGHTENED
    @Test
    public void getGhostsTest1() {
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        pacmanGame.getGhosts().get(0).setPhase(Phase.SCATTER, 1);
        assertEquals(pacmanGame.getGhosts().get(0).getColour(),
                boardViewModel.getGhosts().get(0).getValue());
    }

    // test when ghost's phase is FRIGHTENED
    @Test
    public void getGhostsTest2() {
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        pacmanGame.getGhosts().get(0).setPhase(Phase.FRIGHTENED, 1);
        assertEquals("#0000FF",
                boardViewModel.getGhosts().get(0).getValue());
    }

    // test all the ghost
    @Test
    public void getGhostsTest3() {
        BoardViewModel boardViewModel = new BoardViewModel(pacmanGame);
        pacmanGame.getGhosts().get(0).setPhase(Phase.SCATTER, 1);
        pacmanGame.getGhosts().get(1).setPhase(Phase.SCATTER, 1);
        pacmanGame.getGhosts().get(2).setPhase(Phase.SCATTER, 1);
        pacmanGame.getGhosts().get(3).setPhase(Phase.SCATTER, 1);
        pacmanGame.getGhosts().get(3).setPosition(new Position(1, 3));
        int i = 0;
        for (Ghost ghost :  pacmanGame.getGhosts()) {
            Pair<Position, String> ghostPair =
                    new Pair<>(ghost.getPosition(), ghost.getColour());
            assertEquals(ghostPair, boardViewModel.getGhosts().get(i));
            i++;
        }

    }
}