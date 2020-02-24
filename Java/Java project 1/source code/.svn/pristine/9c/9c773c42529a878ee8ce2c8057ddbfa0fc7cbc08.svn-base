package pacman.game;

import pacman.board.PacmanBoard;
import pacman.score.ScoreBoard;

import java.io.IOException;
import java.io.Writer;

/**
 * Writes the PacmanGame to a standard format.
 * @ass2
 */
public class GameWriter {

    /**
     * Saves a PacmanGame to a writer using the following rules:
     * <p>
     * The first line of the file will be the Board block header:
     * "[Board]". Following this on the line below will be the width
     * and height comma separated with no leading zeros and no spaces.
     * After this on the next line is the Game Board which is to be
     * the toString representation of the board.
     * </p>
     * One blank line.
     * <p>
     * On the next line is the "[Game]" block which
     * will output the following assignments in order ( title, author,
     * lives, level, score, hunter, blinky, inky, pinky, clyde ). The
     * assignments are to have a single space before and after the
     * equals sign. The assignments for ( hunter, blinky, inky, pinky,
     * clyde) are to be the toString representation of these entities.
     * Each assignment is to be on its own line.
     * </p>
     * One blank line.
     * <p>
     * The last block is the "[Scores]" block which should be output
     * as a multiline list of the scores where the name and value of
     * the score are sperated by a ":". The scores should be output
     * sorted by Name {@link ScoreBoard#getEntriesByName()}. The last
     * score should not have a newline.
     * </p>
     * <p>
     *     Note: All integers are to have no leading zeros.
     * </p>
     * @param writer to output the data to.
     * @param game to encode into the save data format.
     * @throws IOException during an issue with saving to the file.
     * @ass2
     */
    public static void write(Writer writer, PacmanGame game)
            throws IOException {
        writeBoardBlock(writer, game.getBoard());
        writer.write(System.lineSeparator());
        writeGameBlock(writer, game);
        writer.write(System.lineSeparator());
        writeScoresBlock(writer, game.getScores());
    }

    /*
     * Writes out a Board Block.
     */
    private static void writeBoardBlock(Writer writer, PacmanBoard board)
            throws IOException {
        
        writer.write(String.format("[Board]%n"));
        writer.write(String.format("%d,%d%n", 
                board.getWidth(), board.getHeight()));
        writer.write(board.toString());
        writer.write(System.lineSeparator());
    }

    /*
     * Writes out a scores block.
     */
    private static void writeScoresBlock(Writer writer, ScoreBoard scores)
            throws IOException {
        
        writer.write(String.format("[Scores]%n"));
        
        // separate score entries with newlines
        String output = String.join(System.lineSeparator(), scores.getEntriesByName());
        writer.write(output);
    }

    /*
     * Writes out a Game block.
     */
    private static void writeGameBlock(Writer writer, PacmanGame game)
            throws IOException {
        
        writer.write(String.format("[Game]%n"));
        writer.write(String.format("title = %s%n",
                game.getTitle()));
        writer.write(String.format("author = %s%n",
                game.getAuthor()));
        writer.write(String.format("lives = %d%n",
                game.getLives()));
        writer.write(String.format("level = %d%n",
                game.getLevel()));
        writer.write(String.format("score = %d%n",
                game.getScores().getScore()));

        writer.write(String.format("hunter = %s%n",
                game.getHunter().toString()));
        
        // write ghosts
        for (var ghost : game.getGhosts()) {
            writer.write(String.format("%s = %s%n",
                    ghost.getType().toString().toLowerCase(),
                    ghost.toString()));
        }
    }

}
