package game;

import csse2002.block.world.Action;
import csse2002.block.world.ActionFormatException;
import csse2002.block.world.Position;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * The controller for all the buttons from View
 */
public class Controller {
    /* A view contains all the components */
    private View view;

    /* A byteArrayOutPutStream contains the stdout */
    private ByteArrayOutputStream outContent;

    /**
     * The construct of controller basic on the view
     *
     * @param view :   view contains all the application components
     */
    public Controller(View view) {
        this.view = view;
        view.addButtonHandler(new ButtonHandler());
    }

    /**
     * A private class under Controller class, which will implement all the
     * buttons' actions
     */
    private class ButtonHandler implements EventHandler<ActionEvent> {

        /**
         * The button handler, which will deal all different buttons by check
         * the source
         * Give each button their own actions
         *
         * @param event :   to find the source
         */
        @Override
        public void handle(ActionEvent event) {
            outContent = new ByteArrayOutputStream();
            Button pressedButton = (Button) event.getSource();
            System.setOut(new PrintStream(outContent));
            List<String> directions = new LinkedList<>();
            directions.add("north");
            directions.add("west");
            directions.add("south");
            directions.add("east");
            if (directions.contains(pressedButton.getText())) {
                if (view.getComboBox().getValue().equals("Move Builder")) {
                    moveBuilderAction(pressedButton);
                } else {
                    moveBlockAction(pressedButton);
                }
            } else if (pressedButton.getText().equals("Dig")) {
                digAction();
            } else if (pressedButton.getText().equals("Drop")) {
                dropAction();
            }
            System.out.flush();
            outContent = new ByteArrayOutputStream();
        }

        /**
         * Move builder action, when player click on any direction button,
         * and th combo box is choosing "move builder"
         * Change the current tile and the start position, then call the
         * drawMap method to update the canvas
         * If this action is invalid, prompt player by open an alert window
         *
         * @param pressedButton :   The source button, which can help to
         *                      figure out the direction that player want to
         *                      explore.
         */
        public void moveBuilderAction(Button pressedButton) {
            String direction = pressedButton.getText();
            String line = "MOVE_BUILDER " + direction;
            InputStream is = new ByteArrayInputStream((line.getBytes()));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            boolean flag = true;
            if (view.getWorldMap().getBuilder().getCurrentTile().getExits()
                    .containsKey(direction)) {
                if (view.getWorldMap().getBuilder().getCurrentTile()
                        .getExits().get(direction).getBlocks().size() - view
                        .getWorldMap().getBuilder().getCurrentTile()
                        .getBlocks().size() > 1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Cannot move builder");
                    alert.setHeaderText(null);
                    alert.setContentText("Target tile is too high");
                    alert.showAndWait();
                    flag = false;
                } else if (view.getWorldMap().getBuilder().getCurrentTile()
                        .getExits().get(direction).getBlocks().size() - view
                        .getWorldMap().getBuilder().getCurrentTile()
                        .getBlocks().size() < -1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Cannot move builder");
                    alert.setHeaderText(null);
                    alert.setContentText("Target tile is too low");
                    alert.showAndWait();
                    flag = false;
                }
            }
            if (flag) {
                try {
                    Action.processAction(Action.loadAction(br), view
                            .getWorldMap());
                    String infoString = "";
                    switch (outContent.toString()) {
                        case "No exit this way\n":
                            infoString = "No exit in the intended direction";
                            break;
                        case "Too high\n":
                            infoString = "Target tile is too high";
                            break;
                    }
                    if (!infoString.equals("")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Cannot move builder");
                        alert.setHeaderText(null);
                        alert.setContentText(infoString);
                        alert.showAndWait();
                    } else {
                        view.drawMap();
                    }
                } catch (ActionFormatException e) {
                    e.printStackTrace();
                }
            }
            if (direction.equals("north")) {
                view.startPosition = new Position(view.startPosition.getX(),
                        view.startPosition.getY() - 1);
            } else if (direction.equals("south")) {
                view.startPosition = new Position(view.startPosition.getX(),
                        view.startPosition.getY() + 1);
            } else if (direction.equals("west")) {
                view.startPosition = new Position(view.startPosition.getX()
                        - 1,
                        view.startPosition.getY());
            } else if (direction.equals("east")) {
                view.startPosition = new Position(view.startPosition.getX()
                        + 1,
                        view.startPosition.getY());
            }
        }

        /**
         * Move block action, when player click on any direction button,
         * and th combo box is choosing "move block"
         * change the block of current tile and the target tile
         * Drop any block by using the dropIndex
         * If this action is invalid, prompt player by open an alert window
         *
         * @param pressedButton :   The source button, which can help to
         *                      figure out the direction that player want to
         *                      explore.
         */
        public void moveBlockAction(Button pressedButton) {
            String direction = pressedButton.getText();
            String line = "MOVE_BLOCK " + direction;
            InputStream is = new ByteArrayInputStream((line.getBytes()));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                Action.processAction(Action.loadAction(br), view.getWorldMap
                        ());
                String infoString = "";
                switch (outContent.toString()) {
                    case "No exit this way\n":
                        infoString = "No exit in the intended direction";
                        break;
                    case "Too high\n":
                        infoString = "Target tile is too high";
                        break;
                    case "Cannot use that block\n":
                        infoString = "Top block is not movable";
                }
                if (!infoString.equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Cannot move block");
                    alert.setHeaderText(null);
                    alert.setContentText(infoString);
                    alert.showAndWait();
                } else {
                    view.drawMap();
                }
            } catch (ActionFormatException e) {
                e.printStackTrace();
            }
        }

        /**
         * Dig the top block
         * Then call the drawMap() to update the canvas
         */
        public void digAction() {
            String line = "DIG";
            InputStream is = new ByteArrayInputStream((line.getBytes()));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                Action.processAction(Action.loadAction(br), view.getWorldMap
                        ());
                String infoString = "";
                switch (outContent.toString()) {
                    case "Too low\n":
                        infoString = "There is nothing that you can " +
                                "dig";
                        break;
                    case "Cannot use that block\n":
                        infoString = "The top block is not diggable";
                }
                if (!infoString.equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Cannot dig block");
                    alert.setHeaderText(null);
                    alert.setContentText(infoString);
                    alert.showAndWait();
                } else {
                    view.drawMap();
                    view.updateInv();
                }

            } catch (ActionFormatException e) {
                e.printStackTrace();
            }
        }

        /**
         * Drop any block by using the dropIndex
         * If this action is invalid, prompt player by open an alert window
         */
        public void dropAction() {
            String secondaryAction = view.getTextIndex().getText();
            String line = "DROP " + secondaryAction;
            InputStream is = new ByteArrayInputStream((line.getBytes()));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                Action.processAction(Action.loadAction(br), view.getWorldMap
                        ());
                String infoString = "";
                switch (outContent.toString()) {
                    case "Error: Invalid action\n":
                        infoString = "Invalid text is entered in the “Drop " +
                                "index” field";
                        break;
                    case "Too high\n":
                        infoString = "Target tile is too high";
                        break;
                    case "Cannot use that block\n":
                        infoString = "Target block cannot be dropped";
                }
                if (!infoString.equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Cannot drop block");
                    alert.setHeaderText(null);
                    alert.setContentText(infoString);
                    alert.showAndWait();
                } else {
                    view.drawMap();
                    view.updateInv();
                }
                view.getTextIndex().setText("");
            } catch (ActionFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
