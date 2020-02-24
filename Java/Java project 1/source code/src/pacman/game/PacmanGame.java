package pacman.game;

import pacman.board.PacmanBoard;
import pacman.ghost.*;
import pacman.hunter.*;
import pacman.score.ScoreBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * PacmanGame stores the game's state and acts as the model for
 * the entire game.
 *
 * @ass2
 */
public class PacmanGame {

    // default number of lives for player
    private static final int DEFAULT_LIVES = 4;
    // score increase for ghost kill
    private static final int GHOST_SCORE = 200;

    // title and author of this game
    private String title;
    private String author;

    // the game board
    private PacmanBoard board;

    // the game entities
    private Hunter hunter;
    private List<Ghost> ghosts = new ArrayList<>();
    
    // level state
    private int level = 0;
    private int lives = DEFAULT_LIVES;
    private int tick = 0;

    // scores for the game
    private ScoreBoard scoreBoard = new ScoreBoard();

    /**
     * Creates a new game with the given parameters and spawns one of
     * each type of ghost (Blinky, Clyde, Inky, Pinky). The ghosts
     * should be spawned at the ghost spawn point.
     * 
     * <p>The game should start with:</p>
     * <ul>
     *     <li>a tick of 0.</li>
     *     <li>a level of 0.</li>
     *     <li>a set of 4 lives.</li>
     *     <li>a empty scoreboard with a initial score of 0.</li>
     * </ul>
     *
     * @requires title != null and author != null and hunter != null
     *      and board != null and board contains a spawn point for Ghosts
     *      and for the hunter.
     * @param title of the game board.
     * @param author of the game board.
     * @param hunter for the current game.
     * @param board to be copied for this game.
     * @ass2
     */
    public PacmanGame(String title, String author, Hunter hunter, 
            PacmanBoard board) {
        
        this.title = title;
        this.author = author;
        this.board = new PacmanBoard(board);
        this.hunter = hunter;

        ghosts.addAll(
                List.of(new Blinky(), new Clyde(), new Inky(), new Pinky()));

        for (Ghost ghost : ghosts) {
            ghost.setPosition(board.getGhostSpawn());
        }
    }

    /**
     * @return title of the map.
     * @ensures result != null
     * @ass2
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return author of the map.
     * @ensures result != null
     * @ass2
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the current pacman board.
     * @return a mutable reference to the board.
     * @ass2
     */
    public PacmanBoard getBoard() {
        return this.board;
    }

    /**
     * Gets the number of times that tick has been called in the current game. 
     * See {@link PacmanGame#tick}
     * @return the current game tick value.
     * @ass2
     */
    public int getTick() {
        return this.tick;
    }

    /**
     * Gets the current score board.
     * @return a mutable reference to the score board.
     * @ass2
     */
    public ScoreBoard getScores() {
        return this.scoreBoard;
    }

    /**
     * @return current level of the game.
     * @ass2
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the level of the game.
     *
     * @ensures newLevel = max(0, givenLevel)
     * @param level to be set to.
     * @ass2
     */
    public void setLevel(int level) {
        this.level = Integer.max(0, level);
    }

    /**
     * @return amount of lives the player currently has.
     * @ass2
     */
    public int getLives() {
        return lives;
    }

    /**
     * Sets the lives of the current player.
     * @ensures newLives = max(0, givenLives)
     * @param lives to be set to.
     * @ass2
     */
    public void setLives(int lives) {
        this.lives = Integer.max(0, lives);
    }

    /**
     * @return a mutable reference to the hunter.
     * @ass2
     */
    public Hunter getHunter() {
        return this.hunter;
    }

    /**
     * Note: Adding, removing elements to this list should not affect the
     * internal copy.
     * @return a list of ghosts in the game.
     * @ass2
     */
    public List<Ghost> getGhosts() {
        return new ArrayList<>(this.ghosts);
    }

    /**
     * Tick
     *
     * If we do not have any lives (getLives() == 0) then do nothing.
     * Otherwise we do the following in this order:
     *
     * <ol>
     *     <li>The Hunter moves {@link Hunter#move(PacmanGame)}.</li>
     *     <li>For each ghost in the game, call {@link Hunter#hit(Ghost)} </li>
     *     <li>The Ghosts that are alive move on even ticks 
     *     {@link Ghost#move(PacmanGame)}
     *     {@link PacmanGame#getTick()}.</li>
     *     <li>For each Ghost in the game, call {@link Hunter#hit(Ghost)} 
     * on the game's hunter.</li>
     *     <li>For each ghost which is dead:
     *         <ol>
     *             <li>Reset the ghost. </li>
     *             <li>Set the ghost's position to the ghost spawn
     *             position on the current board. </li>
     *             <li>Add 200 points to the score.</li>
     *         </ol>
     *     </li>
     *     <li>If the hunter is dead, then decrease the lives and reset
     *     all the entities and place them at their spawn points.</li>
     *     <li>If the board is empty, then increase the level and set the 
     *     ticks to 0 and reset the board and entities placing them at their 
     *     spawn points.</li>
     *     <li>If we did not increase the level then increase the tick value.
     *     See {@link PacmanGame#getTick()}</li>
     * </ol>
     * Note: game should start at a tick count of zero. 
     * @ass2
     */
    public void tick() {
        if (lives == 0) {
            return; // do nothing
        }
        
        this.hunter.move(this);
        
        // Check if we killed any ghost
        for (var ghost : ghosts) {
            hunter.hit(ghost);
        }
        
        // move each ghost
        if (tick % 2 == 0) {
            for (var ghost : ghosts) {
                if (!ghost.isDead()) {
                    ghost.move(this);
                }
            }
        }
        
        // check if pacman is colliding with a ghost
        for (var ghost : ghosts) {
            hunter.hit(ghost);
        }

        // respawn ghosts.
        for (var ghost : ghosts) {
            if (ghost.isDead()) {
                ghost.reset();
                ghost.setPosition(board.getGhostSpawn());
                scoreBoard.increaseScore(GHOST_SCORE);
            }
        }

        if (hunter.isDead()) {
            setLives(lives - 1);
            reset(ResetLevel.ENTITIES);
        }

        // next level
        if (board.isEmpty()) {
            setLevel(level + 1);
            reset(ResetLevel.LEVEL);
        } else {
            tick++;
        }
    }

    /**
     * Resets the Game in the following way:
     *
     * <ul>
     *     <li>Lives is set to the default of 4.</li>
     *     <li>Level is set to 0.</li>
     *     <li>ScoreBoard is reset  {@link ScoreBoard#reset()}</li>
     *     <li>PacmanBoard is reset {@link PacmanBoard#reset()}</li>
     *     <li>All entities are reset</li>
     *     <li>All entity positions are set to their spawn locations.</li>
     *     <li>The tick value is reset to zero.</li>
     * </ul>
     * @ass2
     */
    public void reset() {
        reset(ResetLevel.GAME);
    }

    /*
     * TODO
     * 
     */
    private void reset(ResetLevel level) {
        switch (level) {
            case GAME:
                setLives(DEFAULT_LIVES);
                setLevel(0);
                scoreBoard.reset();
                /* fallthrough */
            case LEVEL:
                board.reset();
                tick = 0;
                /* fallthrough */
            case ENTITIES:
                hunter.reset();
                hunter.setPosition(board.getPacmanSpawn());
                
                // reset ghosts
                for (var ghost : ghosts) {
                    ghost.reset();
                    ghost.setPosition(board.getGhostSpawn());
                }
                
                break;
        }
    }

    /**
     * For each ghost in the game, set its phase to be
     * Phase.FRIGHTENED with a duration of
     * Phase.FRIGHTENED.getDuration();
     * @ass2
     */
    public void setGhostsFrightened() {
        for (var ghost : ghosts) {
            ghost.setPhase(Phase.FRIGHTENED, Phase.FRIGHTENED.getDuration());
        }
    }


    /*
     * Helper enum to indicate which part of game to reset.
     */
    private enum ResetLevel {
        // resets entities
        ENTITIES,
        // resets the entire level 
        LEVEL,
        // resets the entire game
        GAME
    }
}
