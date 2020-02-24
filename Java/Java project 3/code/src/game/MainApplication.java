package game;

import javafx.stage.Stage;

/**
 * The main entry of this application, which is a javafx application
 */
public class MainApplication extends javafx.application.Application {

    /**
     * Launch this application
     * The main method of this application
     *
     * @param args -   the argument
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This is the main javafx stage and windows of this application.
     * Construct a new View, then add controller to this view.
     *
     * @param primaryStage -   the primary stage which contains all of this
     *                     application
     */
    @Override
    public void start(Stage primaryStage) {
        View view = new View();
        primaryStage.setTitle("Block World");
        primaryStage.setScene(view.getScene());
        // Controller - which contains the most button actions
        Controller controller = new Controller(view);
        primaryStage.show();
    }
}
