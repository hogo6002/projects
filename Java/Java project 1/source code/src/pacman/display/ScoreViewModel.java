package pacman.display;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pacman.game.PacmanGame;

public class ScoreViewModel {
    private PacmanGame pacmanGame;
    private String order;
    private StringProperty currentProperty;
    private StringProperty orderProperty;
    private ObservableList<String> observableList;

    public ScoreViewModel(PacmanGame model) {
        pacmanGame = model;
        order = "Sorted by Name";
        currentProperty = new SimpleStringProperty();
        orderProperty = new SimpleStringProperty();
        observableList =
                FXCollections.observableList
                        (pacmanGame.getScores().getEntriesByName());
        update();
    }

    public void update() {
        currentProperty.set("Score: " + getCurrentScore());
        orderProperty.set(order);
        if (order.equals("Sorted by Name")) {
            observableList.setAll(pacmanGame.getScores().getEntriesByName());
        } else {
            observableList.setAll(pacmanGame.getScores().getEntriesByScore());
        }

    }

    public void switchScoreOrder() {
        if (order.equals("Sorted by Name")) {
            order = "Sorted by Score";
            return;
        }
        order = "Sorted by Name";
    }

    public StringProperty getCurrentScoreProperty() {
        return currentProperty;
    }

    public StringProperty getSortedBy() {
        return orderProperty;
    }

    public ObservableList<String> getScores() {
        return observableList;
    }

    public int getCurrentScore() {
        return pacmanGame.getScores().getScore();
    }

    public void setPlayerScore(String player, int score) {
        pacmanGame.getScores().setScore(player, score);
    }


}
