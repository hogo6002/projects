package pacman.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ScoreBoard contains previous scores and the current score of the
 * PacmanGame.
 *
 * A score is a name and value that a valid name only contains the
 * following characters:
 *
 * <ul>
 *     <li>A to Z</li>
 *     <li>a to z</li>
 *     <li>0 to 9</li>
 * </ul>
 *
 * and must have a length greater than 0.
 *
 * The value is a integer that is equal to or greater than 0.
 *
 * @ass1
 */
public class ScoreBoard {

    // mapping of names to score values
    private HashMap<String, Integer> scores;
    // current overall score
    private int currentScore;

    /**
     * Creates a score board that has no entries and a current score
     * of 0.
     *
     * @ass1
     */
    public ScoreBoard() {
        this.scores = new HashMap<>();
        this.currentScore = 0;
    }

    /**
     * Gets the stored entries ordered by Name in lexicographic order.
     *
     * The format of the list should be:
     *
     * <ol>
     *     <li>Score name with a single space afterwards</li>
     *     <li>A single colon</li>
     *     <li>A space then the value of the score with no leading
     *     zeros.</li>
     * </ol>
     *
     * Example:
     *
     * <pre>{@code
     *     ScoreBoard board = new ScoreBoard();
     *     board.setScore("Fred", 100);
     *     board.setScore("fred", 20);
     *     board.setScore("Fred", 24);
     *
     *     List<String> scores = board.getEntriesByName();
     *     System.out.println(scores);
     *
     *     // this outputs:
     *     // [Fred : 24, fred : 20]
     *
     * }</pre>
     *
     * @return List of scores formatted as "NAME : VALUE" in the order
     * described above or an empty list if no entries are stored.
     * @ass1
     */
    public List<String> getEntriesByName() {
        var items = new ArrayList<>(scores.entrySet());
        items.sort((o1, o2) -> {
            return o1.getKey().compareTo(o2.getKey());
        });

        var scores = new ArrayList<String>();
        for (var entry : items) {
            scores.add(String.format("%s : %d", entry.getKey(),
                    entry.getValue()));
        }
        
        return scores;
    }

    /**
     * Gets the stored entries ordered by the score in descending order
     * ( 9999 first then 9998 and so on ...) then in lexicographic order
     * of the name if the scores match.
     *
     * The format of the list should be:
     *
     * <ol>
     *     <li>Score name with a single space afterwards</li>
     *     <li>A single colon</li>
     *     <li>A space then the value of the score with no leading
     *     zeros.</li>
     * </ol>
     *
     * Example:
     *
     * <pre>{@code
     *     ScoreBoard board = new ScoreBoard();
     *     board.setScore("Alfie", 100);
     *     board.setScore("richard", 20);
     *     board.setScore("Alfie", 24);
     *     board.setScore("ben", 20);
     *
     *     List<String> scores = board.getEntriesByScore();
     *     System.out.println(scores);
     *
     *     // this outputs
     *     // [Alfie : 24, ben : 20, richard : 20]
     * }</pre>
     *
     * @return List of scores formatted as "NAME : VALUE" in the order
     * described above or an empty list if no entries are stored.
     * @ass1
     */
    public List<String> getEntriesByScore() {
        var items = new ArrayList<>(scores.entrySet());
        items.sort((o1, o2) -> {
            var valCompare = o2.getValue().compareTo(o1.getValue());
            
            // Compare by name is score is the same
            if (valCompare == 0) {
                return o1.getKey().compareTo(o2.getKey());
            }
            
            return valCompare;
        });

        var scores = new ArrayList<String>();
        for (var entry : items) {
            scores.add(String.format("%s : %s", entry.getKey(),
                    entry.getValue()));
        }
        
        return scores;
    }

    /**
     * Sets the score for the given name if:
     *
     * <ul>
     *     <li>name is not null</li>
     *     <li>name is a valid score name</li>
     *     <li>score is equal to or greater than zero.</li>
     * </ul>
     *
     * This should override any score stored for the given name if
     * name and score are valid.
     *
     * @param name  of scorer.
     * @param score to set to the given name.
     * @ass1
     */
    public void setScore(String name, int score) {
        if (name != null && !name.isBlank() && validName(name) && score > -1) {
            this.scores.put(name, score);
        }
    }

    /**
     * Sets a collection of scores if "scores" is not null, otherwise
     * no scores are modified.
     *
     * For each score contained in the scores if:
     *
     * <ul>
     *     <li>name is not null</li>
     *     <li>name is a valid score name</li>
     *     <li>score is equal to or greater than zero.</li>
     * </ul>
     *
     * the score will be set and override any stored score for the given
     * name, otherwise it will be skipped.
     *
     * @param scores to add.
     * @ass1
     */
    public void setScores(Map<String, Integer> scores) {
        if (scores != null) {
            for (var score : scores.entrySet()) {
                this.setScore(score.getKey(), score.getValue());
            }
        }
    }

    /**
     * Increases the score if the given additional is greater than 0.
     * No change to the current score if additional is less than or
     * equal to 0.
     *
     * @param additional score to add.
     * @ass1
     */
    public void increaseScore(int additional) {
        currentScore = Integer.max(currentScore, currentScore + additional);
    }

    /**
     * Get the current score.
     *
     * @return the current score.
     * @ass1
     */
    public int getScore() {
        return currentScore;
    }

    /**
     * Set the current score to 0.
     *
     * @ass1
     */
    public void reset() {
        currentScore = 0;
    }

    /**
     * Checks if a given name is valid.
     * A valid name consists of only one or more alphanumeric characters.
     */
    private boolean validName(String name) {
        // regex checks string contains only alphanumericals, and at least 1
        return name.matches("[a-zA-Z0-9]+");
    }

    /**
     * Checks if another object instance is equal to this ScoreBoard. 
     * ScoreBoards are equal if the current scores are equal and all score 
     * entries are equal (both names and score values). 
     * @return true if same, false otherwise.
     * @ass2
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (!(o instanceof ScoreBoard)) {
            return false;
        }
        
        ScoreBoard otherBoard = (ScoreBoard) o;

        return otherBoard.getScore() == currentScore
                && scores.equals(otherBoard.scores);
    }

    /**
     * For two objects that are equal the hash should also
     * be equal. For two objects that are not equal the hash
     * does not have to be different.
     * @return hash of the ScoreBoard.
     * @ass2
     */
    @Override
    public int hashCode() {
        return 31 * scores.hashCode() + 33 * currentScore;
    }
}
