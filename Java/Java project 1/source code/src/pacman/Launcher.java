package pacman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import javafx.application.Application;
import javafx.stage.Stage;
import pacman.display.MainView;
import pacman.display.MainViewModel;
import pacman.game.GameReader;
import pacman.util.UnpackableException;

/**
 * Main entry point for the CSSE2002/7023 PacMan Game.
 * @given
 */
public class Launcher extends Application {

    /**
     * CSSE2002/7023 Pacman
     *
     * Arguments: [FILENAME]
     * @param args from the command line.
     * @given
     */
    public static void main(final String... args) {
        System.out.println("javafx.runtime.version: " + System.getProperties().get("javafx.runtime.version"));
        if (args.length != 2) {
            System.out.println("Usage: [MAP FILENAME] [SAVE FILENAME]");
            System.exit(1);
        }
        Application.launch(Launcher.class, args);
    }

    /**
     * Runs the main gui with the parameters passed via the commandline
     * @param theStage to render to.
     * @throws UnpackableException when file is invalid.
     * @throws IOException when unable to read from file.
     * @given
     */
    @Override
    public void start(Stage theStage) throws UnpackableException, IOException {
        theStage.setResizable(false);
        var params = getParameters().getRaw();

        Reader reader;
        try {
            reader = new BufferedReader(new FileReader(params.get(0)));
        } catch (IOException e) {
            System.err.println(e.toString());
            return;
        }

        var model = GameReader.read(reader);

        var view = new MainView(theStage,
                new MainViewModel(model, params.get(1)));
        view.run();
    }
}
