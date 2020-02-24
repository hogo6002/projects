package pacman.game;

import pacman.board.BoardItem;
import pacman.board.PacmanBoard;
import pacman.ghost.Ghost;
import pacman.ghost.Phase;
import pacman.hunter.*;
import pacman.util.Direction;
import pacman.util.Position;
import pacman.util.UnpackableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * GameReader
 *
 * Reads in a saved games state and returns a game instance.
 * @ass2
 */
public class GameReader {
    
    // The assignments allowed in the game block
    private static final Set<String> GAME_KEYS = Set.of("title", "author", 
            "lives", "level", "score", "hunter", 
            "blinky", "inky", "pinky", "clyde");
    // number of comma separated elements in hunter and ghost value strings
    private static final int HUNTER_STRING_ITEMS = 5;
    private static final int GHOST_STRING_ITEMS = 4;

    /**
     * Reads in a game according to the the following specification:
     * <p>
     * Note: any lines starting with a ';' are comments and are
     * skipped.
     * </p>
     * <p>
     *     A Game File has 3 blocks ( Board, Game, Scores ) which must
     *     be in order of Board first, Game second and Scores last. Each
     *     block is defined by its name enclosed in square brackets.
     *     e.g: [Board], [Game], [Scores]
     * </p>
     *
     * <p>
     * There must be no empty lines before the first block and
     * in between blocks there must be a single blank line.
     * </p>
     *
     * <h3>[Board]</h3>
     * <p>
     * First line in the block is a comma separated width and height.
     * There must be (height) lines of (width) following the first line.
     * Each character in these lines (after stripping) must be of
     * the BoardItem keys.
     * </p>
     * <h3>[Game]</h3>
     * <p>
     * Contains newline separated list of assignments in any order
     * which are unique. An assignment is a 'Key = Value' where the
     * Key and Value should have all whitespace removed before reading.
     * The following assignments are also all mandatory otherwise the file
     * is invalid.
     * </p>
     <table border="1">
     *   <caption>Assignment Definitions</caption>
     *   <tr>
     *     <td> Name </td>
     *     <td> Value format </td>
     *   </tr>
     *   <tr><td>title</td><td>string</td></tr>
     *   <tr><td>author</td><td>string</td></tr>
     *   <tr><td>lives</td><td>Integer greater than or equal to zero</td></tr>
     *   <tr><td>level</td><td>Integer greater than or equal to zero</td></tr>
     *   <tr><td>score</td><td>Integer greater than or equal to zero</td></tr>
     *   <tr><td>hunter</td><td>A comma separated list of attributes in the
     *      following order: 
     *      <br>x, y, DIRECTION, special duration, HunterType<br>
     *      where x and y are integers, DIRECTION is the string representation
     *      of a DIRECTION and the special duration is a integer greater than
     *      or equal to zero.</td></tr>
     *   <tr><td>blinky|inky|pinky|clyde</td><td>A comma separated list of 
     *      attributes in the following order: 
     *      <br>x,y,DIRECTION,PHASE:PhaseDuration<br>
     *      where x and y are Integers, DIRECTION is the string representation
     *      of a DIRECTION and PHASE is a string representation of the 
     *      PhaseType with a duration that is an Integer greater than 
     *      or equal to zero
     *      </td></tr>
     * </table>
     * <h3>[Scores]</h3>
     * <p>
     * A newline seperated list of unique scores in the form of:
     * 'Name : Value' where name is a string and value is an Integer.
     * </p>
     * @param reader to read the save game from.
     * @return a PacmanGame that reflects the state from the reader.
     * @throws UnpackableException when the saved data is invalid.
     * @throws IOException when unable to read from the reader.
     * @ass2
     */
    public static PacmanGame read(Reader reader) throws UnpackableException,
            IOException {
        var betterReader = new SmoothReader(reader);
        
        var board = readGameBoard(betterReader);
        var game = readGame(betterReader, board);
        var scores = readScores(betterReader);

        game.getScores().setScores(scores);

        return game;
    }

    /*
     * Reads a game board.
     */
    private static PacmanBoard readGameBoard(SmoothReader reader)
            throws IOException, UnpackableException {
        
        String line = reader.readLine();
        
        if (!line.equals("[Board]")) {
            throw new UnpackableException("Was expecting [Board] header");
        }
        
        line = reader.readLine();
        var dimensions = line.split(",");
        if (dimensions.length != 2) {
            throw new UnpackableException("Invalid board dimensions");
        }

        PacmanBoard board;
        try {
            board = new PacmanBoard(Integer.parseInt(dimensions[0]),
                    Integer.parseInt(dimensions[1]));
        } catch (IllegalArgumentException e) {
            throw new UnpackableException("Invalid board dimensions");
        }

        // read grid
        line = reader.readLine();
        int y = 0;
        while (line != null && !line.isEmpty()) {
            if (line.length() != board.getWidth()) {
                throw new UnpackableException("Incorrect board line length");
            }
            for (int x = 0; x < line.length(); x++) {
                try {
                    board.setEntry(new Position(x, y),
                            BoardItem.getItem(line.charAt(x)));
                } catch (IllegalArgumentException e) {
                    throw new UnpackableException("Invalid item in board");
                }
            }
            line = reader.readLine();
            y++;
        }

        // check given height matched board grid given
        if (y != board.getHeight()) {
            throw new UnpackableException("Incorrect board height");
        }

        return board;
    }

    /*
     * Reads the assignments in the [Game] block from reader and creates
     * a PacmanGame
     */
    private static PacmanGame readGame(SmoothReader reader, PacmanBoard board)
            throws UnpackableException, IOException {
        
        var blockName = reader.readLine();
        if (!blockName.equals("[Game]")) {
            throw new UnpackableException("Was expecting [Game] header");
        }

        var assignments = readBlock(reader);
        if (!assignments.getKeys().equals(GAME_KEYS)) {
            throw new UnpackableException("Missing elements in game block.");
        }

        // create the game
        var game = new PacmanGame(
                assignments.getValue("title"),
                assignments.getValue("author"),
                createHunter(assignments.getValue("hunter"), board),
                board);

        for (var key : assignments.getKeys()) {
            switch (key) {
                case "lives":
                    game.setLives(assignments.getNonNegativeInt(key));
                    break;
                case "level":
                    game.setLevel(assignments.getNonNegativeInt(key));
                    break;
                case "score":
                    game.getScores().increaseScore(
                            assignments.getNonNegativeInt(key));
                    break;
                case "blinky":
                case "clyde":
                case "inky":
                case "pinky":
                    setGhost(game, key, assignments.getValue(key));
                    break;
                default:
                    break; // no need to do anything.
            }
        }

        return game;
    }
    
    /*
     * Turns a hunter info string into a Hunter instance.
     * Uses the given board to determine if the hunter's position is valid.
     */
    private static Hunter createHunter(String info, PacmanBoard board)
            throws UnpackableException {
        
        Hunter hunter;
        var parts = info.split(",");
        if (parts.length != HUNTER_STRING_ITEMS) {
            throw new UnpackableException("Invalid hunter assignment");
        }
        
        HunterType type;
        try {
            type = HunterType.valueOf(parts[4]);
        } catch (IllegalArgumentException e) {
            throw new UnpackableException("Invalid Hunter type");
        }

        switch (type) {
            case SPEEDY:
                hunter = new Speedy();
                break;
            case PHASEY:
                hunter = new Phasey();
                break;
            case HUNGRY:
                hunter = new Hungry();
                break;
            case PHIL:
                hunter = new Phil();
                break;
            default:
                throw new UnpackableException("Invalid Hunter type");
        }

        // extract hunter direction/position
        Position position;
        try {
            position = new Position(Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]));
        } catch (NumberFormatException e) {
            throw new UnpackableException("Invalid hunter position");
        }

        if (isPositionOutsideBoard(position, board)) {
            throw new UnpackableException("Hunter position is outside board");
        }

        try {
            hunter.setPosition(position);
            hunter.setDirection(Direction.valueOf(parts[2]));
        } catch (IllegalArgumentException e) {
            throw new UnpackableException("Invalid hunter direction");
        }

        int specialDuration;
        try {
            specialDuration = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            throw new UnpackableException("Invalid special duration");
        }

        if (specialDuration < 0) {
            throw new UnpackableException("Special duration must be >= 0");
        }
        hunter.activateSpecial(specialDuration);

        return hunter;
    }

    /*
     * Sets information relating to the Ghost in the game that corresponds to
     * the key in the given Assignment.
     */
    private static void setGhost(PacmanGame game, String ghostName, 
            String info) throws UnpackableException {
        
        Ghost ghost = null;
        for (var gameGhost : game.getGhosts()) {
            if (gameGhost.getType().name().toLowerCase().equals(ghostName)) {
                ghost = gameGhost;
                break;
            }
        }
        
        if (ghost == null) {
            throw new UnpackableException("Can't find ghost in PacmanGame");
        }

        var parts = info.split(",");
        if (parts.length != GHOST_STRING_ITEMS) {
            throw new UnpackableException("Invalid number of attributes");
        }
        
        // extract position and direction from ghost info
        Position position;
        try {
            position = new Position(Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]));
        } catch (NumberFormatException e) {
            throw new UnpackableException("Invalid ghost position");
        }

        if (isPositionOutsideBoard(position, game.getBoard())) {
            throw new UnpackableException("Ghost position is outside board");
        }

        try {
            ghost.setPosition(position);
            ghost.setDirection(Direction.valueOf(parts[2]));
            
        } catch (IllegalArgumentException e) {
            throw new UnpackableException("Invalid ghost assignment");
        }

        var phaseParts = parts[3].split(":");
        if (phaseParts.length != 2) {
            throw new UnpackableException("Invalid phase attributes");
        }

        int phaseDuration;
        try {
            phaseDuration = Integer.parseInt(phaseParts[1]);
        } catch (NumberFormatException e) {
            throw new UnpackableException("Invalid phase duration");
        }

        if (phaseDuration < 0) {
            throw new UnpackableException("Phase duration must be >= 0");
        }

        try {
            ghost.setPhase(Phase.valueOf(phaseParts[0]), phaseDuration);
        } catch (IllegalArgumentException e) {
            throw new UnpackableException("Invalid phase type");
        }
    }

    /*
     * Reads in a collection of scores.
     */
    private static Map<String, Integer> readScores(SmoothReader reader)
            throws IOException, UnpackableException {
        
        if (!reader.readLine().equals("[Scores]")) {
            throw new UnpackableException("Was expecting [Scores] header");
        }
        
        var scores = new HashMap<String, Integer>();
        
        String line = reader.readLine();
        while (line != null && !line.isEmpty()) {
            var parts = line.split(":");
            if (parts.length != 2) {
                throw new UnpackableException("Missing score value");
            }

            String scoreName = parts[0].strip();
            if (scores.containsKey(scoreName)) {
                throw new UnpackableException("Duplicate score entry");
            }
            
            try {
                scores.put(scoreName, Integer.parseInt(parts[1].strip()));
            } catch (NumberFormatException e) {
                throw new UnpackableException("Invalid score value");
            }

            line = reader.readLine();
        }

        return scores;
    }

    /*
     * Reads a group of assignments.
     */
    private static AssignmentMap readBlock(SmoothReader reader)
            throws IOException, UnpackableException {
        
        var assignments = new AssignmentMap();

        String line = reader.readLine();
        while (line != null && !line.isEmpty()) {
            assignments.addAssignment(line);
            
            line = reader.readLine();
        }
        
        return assignments;
    }

    /*
     * Returns true if the given position is outside the bounds of the given
     * game board, false otherwise.
     */
    private static boolean isPositionOutsideBoard(Position position,
            PacmanBoard board) {
        return position.getX() < 0 || position.getY() < 0
                || position.getX() >= board.getWidth()
                || position.getY() >= board.getHeight();
    }

    /*
     * Helper class to store and read key-value assignments
     */
    private static class AssignmentMap {
        
        // stores the key-value assignments
        private HashMap<String, String> assignments = new HashMap<>();

        /*
         * Reads an assignment from given string and adds to internal map.
         * @throws UnpackableException if not a valid 'key = value' line
         */
        public void addAssignment(String line) throws UnpackableException {
            var parts = line.split("=", 2);
            if (parts.length != 2) {
                throw new UnpackableException("Assignment missing value");
            }
            
            String key = parts[0].strip();
            String value = parts[1].strip();
            if (key.isEmpty() || value.isEmpty()) {
                throw new UnpackableException("Empty assignment key/value");
            }
            
            if (assignments.containsKey(key)) {
                throw new UnpackableException("Duplicate assignment");
            }

            assignments.put(key, value);
        }
        
        /*
         * Gets all the keys currently stored in the map.
         */
        public Set<String> getKeys() {
            return this.assignments.keySet();
        }

        /*
         * Gets the value associated with given key.
         * 
         * @throws UnpackableException if no assignment for given key.
         */
        public String getValue(String key) throws UnpackableException {
            if (!assignments.containsKey(key)) {
                throw new UnpackableException("Assignment not found");
            }
            
            return assignments.get(key);
        }
        
        /*
         * Converts the value associated with given key into int.
         * 
         * @throws UnpackableException if can't convert value to non-negative
         * integer
         */
        public int getNonNegativeInt(String key) throws UnpackableException {
            int value;

            try {
                value = Integer.parseInt(getValue(key));
            } catch (NumberFormatException e) {
                throw new UnpackableException("Couldn't convert to int");
            }
            
            if (value < 0) {
                throw new UnpackableException("Negative integer given");
            }
            
            return value;
        }
    }

    /*
     * Helper class to remove comments for us automatically.
     */
    private static class SmoothReader extends BufferedReader {

        SmoothReader(Reader in) {
            super(in);
        }

        @Override
        public String readLine() throws IOException {
            var line = "";
            do {
                line = super.readLine();
                if (line != null) {
                    line = line.strip();
                }
            } while (line != null && line.startsWith(";"));
            return line;
        }
    }
}
