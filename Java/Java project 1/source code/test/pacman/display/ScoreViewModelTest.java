package pacman.display;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pacman.board.PacmanBoard;
import pacman.game.PacmanGame;
import pacman.hunter.Hungry;
import pacman.hunter.Hunter;
import pacman.score.ScoreBoard;

import static org.junit.Assert.*;

public class ScoreViewModelTest {
    private PacmanGame pacmanGame;
    private ScoreBoard scoreBoard;

    @Before
    public void setUp() throws Exception {
        Hunter hunter = new Hungry();
        pacmanGame = new PacmanGame("title", "Diane", hunter,
                new PacmanBoard(5, 6));
        scoreBoard = pacmanGame.getScores();
        scoreBoard.setScore("A", 0);
        scoreBoard.setScore("B", 10);
        scoreBoard.setScore("C", 100);
    }

    @After
    public void tearDown() throws Exception {
        pacmanGame = null;
    }

    @Test
    public void constructorTest() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        assertEquals("Sorted by Name",
                scoreViewModel.getSortedBy().getValue());
    }


    @Test
    public void updateTest() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        assertEquals("Sorted by Name",
                scoreViewModel.getSortedBy().getValue());
        scoreViewModel.switchScoreOrder();
        assertFalse(scoreViewModel.getSortedBy().
                getValue().equals("Sorted by Score")); // have not called
        // update
        scoreViewModel.update();
        assertEquals("Sorted by Score",
                scoreViewModel.getSortedBy().getValue());
        assertEquals(scoreBoard.getEntriesByScore(),
                scoreViewModel.getScores());
    }

    @Test
    public void switchScoreOrder() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        scoreViewModel.switchScoreOrder();
        scoreViewModel.update();
        assertEquals("Sorted by Score",
                scoreViewModel.getSortedBy().getValue());
    }

    @Test
    public void getCurrentScorePropertyTest() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        assertEquals("Score: 0",
                scoreViewModel.getCurrentScoreProperty().getValue());
    }

    // test after called update().
    @Test
    public void getCurrentScorePropertyTest1() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        scoreBoard.increaseScore(20);
        scoreViewModel.update();
        assertEquals("Score: 20",
                scoreViewModel.getCurrentScoreProperty().getValue());
    }

    // test when order is by name.
    @Test
    public void getSortedByTest() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        assertEquals("Sorted by Name",
                scoreViewModel.getSortedBy().getValue());
    }

    // test when order is by Score.
    @Test
    public void getSortedByTest1() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        scoreViewModel.switchScoreOrder();
        scoreViewModel.update();
        assertEquals("Sorted by Score",
                scoreViewModel.getSortedBy().getValue());
    }

    // test when order is by name.
    @Test
    public void getScoresTest1() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        assertEquals(scoreBoard.getEntriesByName(),
                scoreViewModel.getScores());
    }

    // test when order is by score.
    @Test
    public void getScoresTest2() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        scoreViewModel.switchScoreOrder();
        scoreViewModel.update();
        assertEquals(scoreBoard.getEntriesByScore(),
                scoreViewModel.getScores());
    }

    @Test
    public void getCurrentScore() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        assertEquals(0, scoreViewModel.getCurrentScore());
    }

    // test when player's name is valid
    @Test
    public void setPlayerScoreTest() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        scoreViewModel.setPlayerScore("D", 10000);
        scoreViewModel.update();
        assertEquals("D : 10000", scoreViewModel.getScores().get(3));
    }

    // test when player's name is invalid
    @Test
    public void setPlayerScoreTest1() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        assertEquals(3, scoreViewModel.getScores().size());
        scoreViewModel.setPlayerScore("../.;D", 10000);
        scoreViewModel.update();
        assertEquals(3, scoreViewModel.getScores().size());
    }

    // test override previously set.
    @Test
    public void setPlayerScoreTest2() {
        ScoreViewModel scoreViewModel = new ScoreViewModel(pacmanGame);
        scoreViewModel.setPlayerScore("C", 10000);
        scoreViewModel.update();
        assertEquals("C : 10000", scoreViewModel.getScores().get(2));
    }
}