import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;

import java.util.Optional;

public class View {
    public Button[] dirButtons;
    public Button[] funButtons;
    private BorderPane rootBox;
    public Cartographer cartographer;
    public TextArea message;


    /**
     * Constructor
     */
    public View() {
        rootBox = new BorderPane();
        addComponents();
    }

    public void addButtonHandler(EventHandler<ActionEvent> handler) {
        /*
         * Adds a handler to the setOnAction meaning when buttons is pressed her
         * this handler and its handle method
         */
        for (Button b : dirButtons) {
            b.setOnAction(handler);
        }

        for (Button b : funButtons) {
            b.setOnAction(handler);
        }
    }


    public void addTopComponents(BorderPane top) {
        GridPane right = new GridPane();
        dirButtons = new Button[4];
        dirButtons[0] = new Button("North");
        dirButtons[1] = new Button("South");
        dirButtons[2] = new Button("West");
        dirButtons[3] = new Button("East");

        funButtons = new Button[6];
        funButtons[0] = new Button("Look");
        funButtons[1] = new Button("Examine");
        funButtons[2] = new Button("Drop");
        funButtons[3] = new Button("Take");
        funButtons[4] = new Button("Fight");
        funButtons[5] = new Button("Save");

        right.add(dirButtons[0], 1, 0);
        right.add(dirButtons[1], 1, 2);
        right.add(dirButtons[2], 0, 1);
        right.add(dirButtons[3], 2, 1);

        right.add(funButtons[0], 0, 3);
        right.add(funButtons[1], 1, 3, 3, 1);
        right.add(funButtons[2], 0, 4);
        right.add(funButtons[3], 1, 4);
        right.add(funButtons[4], 0, 5);
        right.add(funButtons[5], 0, 6);

        cartographer = new Cartographer();
        top.setCenter(cartographer.getRoot());
        top.setRight(right);

    }

    public TextArea addBottomComponents() {
        message = new TextArea();
        message.appendText("You find yourself in " + cartographer.getStartRoom().getDescription());

        return message;
    }

    public void addComponents() {
        BorderPane top = new BorderPane();
        addTopComponents(top);
        TextArea bottom = new TextArea();
        bottom = addBottomComponents();

        rootBox.setTop(top);
        rootBox.setBottom(bottom);
    }

    public void move() {
        cartographer.reDraw();
    }


    public Scene getScene() {
        return new Scene(rootBox);
    }

}
