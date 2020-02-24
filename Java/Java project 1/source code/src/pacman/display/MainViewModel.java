package pacman.display;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pacman.game.GameWriter;
import pacman.game.PacmanGame;
import pacman.hunter.Hunter;
import pacman.util.Direction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainViewModel {
    private PacmanGame pacmanGame;
    private String saveFile;
    private ScoreViewModel scoreViewModel;
    private BoardViewModel boardViewModel;
    private BooleanProperty isPaused;
    private BooleanProperty isOver;
    private StringProperty title;
    private int tick;

    public MainViewModel(PacmanGame model,
                         String saveFilename) {
        pacmanGame = model;
        saveFile = saveFilename;
        scoreViewModel = new ScoreViewModel(pacmanGame);
        boardViewModel = new BoardViewModel(pacmanGame);
        isPaused = new SimpleBooleanProperty(true);
        isOver = new SimpleBooleanProperty(false);
        title = new SimpleStringProperty();
        tick = 0;
        update();
    }

    public void update() {
        title.set(pacmanGame.getTitle() + " by " + pacmanGame.getAuthor());
        scoreViewModel.update();
    }

    public StringProperty getTitle() {
        return title;
    }

    public BooleanProperty isGameOver() {
        return isOver;
    }

    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(new File(saveFile));
            GameWriter.write(fileWriter, pacmanGame);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tick() {
        if (!isPaused.get()) {
            int delay;
            if (pacmanGame.getLevel() == 0 || pacmanGame.getLevel() == 1) {
                delay = 50;
            } else if (pacmanGame.getLevel() == 2 || pacmanGame.getLevel() == 3) {
                delay = 40;
            } else if (pacmanGame.getLevel() == 4 || pacmanGame.getLevel() == 5) {
                delay = 30;
            } else if (pacmanGame.getLevel() > 8) {
                delay = 10;
            } else {
                delay = 20;
            }
            if (tick % delay == 0) {
                pacmanGame.tick();
            }
            tick++;
        }
        if (pacmanGame.getLives() == 0) {
            isOver.set(true);
        }
    }

    public void accept(String input) {
        switch (input) {
            case "P":
            case "p":
                isPaused.set(!isPaused.getValue());
                break;
            case "R":
            case "r":
                pacmanGame.reset();
                break;
        }
        if (!isPaused.getValue()) {
            switch (input) {
                case "A":
                case "a":
                    pacmanGame.getHunter().setDirection(Direction.LEFT);
                    break;
                case "D":
                case "d":
                    pacmanGame.getHunter().setDirection(Direction.RIGHT);
                    break;
                case "W":
                case "w":
                    pacmanGame.getHunter().setDirection(Direction.UP);
                    break;
                case "S":
                case "s":
                    pacmanGame.getHunter().setDirection(Direction.DOWN);
                    break;
                case "O":
                case "o":
                    pacmanGame.getHunter().
                            activateSpecial(Hunter.SPECIAL_DURATION);
            }
        }

    }

    public BooleanProperty isPaused() {
        return isPaused;
    }

    public ScoreViewModel getScoreVM() {
        return scoreViewModel;
    }

    public BoardViewModel getBoardVM() {
        return boardViewModel;
    }
}
