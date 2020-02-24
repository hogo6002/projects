package game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import csse2002.block.world.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * The view class, which will be called by MainApplication
 * Contains all of the content of this stage
 * This class will create the GUI view
 */
public class View {
    /* All move buttons, contains "north", "south", "west" and "east" */
    private Button[] moveButtons;

    /* The digButton, which can be called to dig block */
    private Button digButton;

    /* The dropButton, which can be called to drop block */
    private Button dropButton;

    /* The graphics context, which is a 2D canvas */
    private GraphicsContext context;

    /* The root box, which is a border pane, contains left and right box */
    private BorderPane rootBox;

    /* The left side box, which contains all the buttons */
    private VBox leftBox;

    /* The root side box, which contains menu, canvas and inventory */
    private VBox rightBox;

    /* A static final integer, which represent the rectangle length 40 */
    private static final int REC_LENGTH = 40;

    /* The builder, which plays this game */
    private Builder builder;

    /**
     * The startPosition, which is also the position of builder's current
     * tile
     */
    public Position startPosition;

    /* The world map in this game */
    private WorldMap worldMap;

    /* The combo box, whcih contains "move builder" and "move block" */
    private ComboBox comboBox;

    /**
     * A text field, which will be received a number from user as drop
     * index
     */
    private TextField dropIndex;

    /* The menu item, which can be used to load map */
    private MenuItem loadButton;

    /* The menu item, which can be used to save map */
    private MenuItem saveButton;

    /* The filename of this world map */
    private String filename;

    /* The instance of sparseTileArray, which can be used to add linked tile */
    private SparseTileArray sta;

    /**
     * The construct of View
     * initialize rootBox and moveButtons
     * add all content
     */
    public View() {
        rootBox = new BorderPane();
        moveButtons = new Button[4];
        addContent();
    }

    /**
     * Get current world map
     *
     * @return - worldMap    :   can be used to find current builder and
     * so on
     */
    public WorldMap getWorldMap() {
        return worldMap;
    }

    /**
     * Add all the content
     * Add leftBox and rightBox into rootBox
     */
    public void addContent() {
        rightBox = new VBox();
        leftBox = new VBox();
        addLeftContent();
        addRightContent();
        rootBox.setLeft(leftBox);
        rootBox.setRight(rightBox);
        initialInv();
    }

    /**
     * Update the inventory label
     * Show player the current inventory items
     * change the label to scroll pane once user has so much inventory.
     */
    public void updateInv() {
        VBox labelBox = new VBox();
        Label inventoryLabel = new Label("Builder Inventory:");
        inventoryLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        String inventoryStr = inventoryString();
        Label inventoryContent = new Label(inventoryStr);
        if (inventoryStr.length() > 92) {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(inventoryContent);
            labelBox.getChildren().addAll(inventoryLabel, scrollPane);
        } else {
            labelBox.getChildren().addAll(inventoryLabel, inventoryContent);
        }
        inventoryContent.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        Insets insets = new Insets(0, 20, 50, 30);
        labelBox.setPadding(insets);
        labelBox.setSpacing(2);
        rootBox.setBottom(labelBox);
    }

    /**
     * Initialize the inventory label
     * It will be used when the game hasn't been load
     */
    public void initialInv() {
        VBox labelBox = new VBox();
        Label inventoryLabel = new Label("Builder Inventory:");
        inventoryLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        Label inventoryContent = new Label("[]");
        labelBox.getChildren().addAll(inventoryLabel, inventoryContent);
        inventoryContent.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        Insets insets = new Insets(0, 0, 50, 30);
        labelBox.setPadding(insets);
        labelBox.setSpacing(2);
        rootBox.setBottom(labelBox);
    }

    /**
     * To setup when game is start
     * load this world map, and draw all tiles, builder and exits into the
     * canvas
     * set disable false to all buttons, so that user are supposed to click
     * any of them
     *
     * @param filename :   The filename of this worldmap, which can be used
     *                 to create a new world map
     */
    public void gameStart(String filename) {
        try {
            worldMap = new WorldMap(filename);
            startPosition = worldMap.getStartPosition();
            builder = worldMap.getBuilder();
            for (int i = 0; i < moveButtons.length; i++) {
                moveButtons[i].setDisable(false);
            }
            dropIndex.setDisable(false);
            comboBox.setDisable(false);
            digButton.setDisable(false);
            dropButton.setDisable(false);
            drawMap();
            updateInv();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Map loaded");
            alert.setHeaderText(null);
            alert.setContentText("New map is successfully loaded");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot load map");
            alert.setHeaderText(null);
            alert.setContentText("Invalid map, cannot be loaded");
            alert.showAndWait();
        }
    }

    /**
     * Add all left content into one box
     * The left content will contains a file menu, canvas
     * Give the padding to it and space to each content
     */
    public void addLeftContent() {
        Menu fileMenu = new Menu("file");
        loadButton = new MenuItem("Load Game World");
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                Stage stage = new Stage();
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    filename = file.getPath();
                    gameStart(filename);
                }
            }
        });
        saveButton = new MenuItem("Save World Map");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    sta.addLinkedTiles(builder.getCurrentTile(),
                            startPosition.getX(), startPosition.getY());
                    worldMap = new WorldMap(builder.getCurrentTile(),
                            startPosition, builder);
                    FileChooser fileChooser = new FileChooser();
                    Stage stage = new Stage();
                    File file = fileChooser.showSaveDialog(stage);
                    String saveFile;
                    if (file != null) {
                        saveFile = file.getPath();
                        worldMap.saveMap(saveFile);
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Map save");
                    alert.setHeaderText(null);
                    alert.setContentText("Please load map first before save " +
                            "it");
                    alert.showAndWait();
                }
            }
        });
        fileMenu.getItems().addAll(loadButton, saveButton);
        MenuBar menuBar = new MenuBar(fileMenu);
        menuBar.setMaxWidth(55);
        Canvas canvas = new Canvas(360, 360);
        context = canvas.getGraphicsContext2D();
        HBox canvasContainer = new HBox();
        canvasContainer.getChildren().add(canvas);
        canvasContainer.setStyle("-fx-border-color: black");
        VBox leftBottom = new VBox();
        leftBottom.getChildren().addAll(canvasContainer);
        Insets insets = new Insets(0, 0, 15, 30);
        leftBottom.setPadding(insets);
        leftBox.getChildren().addAll(menuBar, leftBottom);
        leftBox.setSpacing(10);
    }

    /**
     * Get the inventory string by find all the blocks which are contained
     * by builder.
     *
     * @return - invenStr    :   The content of this inventory
     */
    public String inventoryString() {
        String invenStr = "[";
        for (int i = 0; i < builder.getInventory().size(); i++) {
            invenStr += builder.getInventory().get(i).getBlockType();
            if (i != builder.getInventory().size() - 1) {
                invenStr += ", ";
            }
        }
        invenStr += "]";
        return invenStr;
    }

    /**
     * Add all right content into one box
     * The right components are move buttons, dig button, drop button and
     * its index text field
     * Set the padding and space to each component
     */
    public void addRightContent() {
        for (int i = 0; i < moveButtons.length; i++) {
            moveButtons[i] = new Button();
            String buttonText = "";
            switch (i) {
                case 0:
                    buttonText = "north";
                    break;
                case 1:
                    buttonText = "west";
                    break;
                case 2:
                    buttonText = "south";
                    break;
                case 3:
                    buttonText = "east";
            }
            moveButtons[i].setText(buttonText);
            moveButtons[i].setPrefWidth(80);
            moveButtons[i].setPrefHeight(40);
            moveButtons[i].setStyle("-fx-background-color: linear-gradient" +
                    "(#3e5ca4, #263868); -fx-font-size: 12px; -fx-text-fill:" +
                    " #FFFFFF; -fx-border-color: #8aa8e5");
            moveButtons[i].setDisable(true);
        }
        GridPane moveGrid = new GridPane();
        moveGrid.add(moveButtons[0], 1, 0);
        moveGrid.add(moveButtons[1], 0, 1);
        moveGrid.add(moveButtons[2], 1, 2);
        moveGrid.add(moveButtons[3], 2, 1);
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Move Builder",
                        "Move Block"
                );
        VBox comboContainer = new VBox();
        comboBox = new ComboBox(options);
        comboBox.setDisable(true);
        comboBox.getSelectionModel().selectFirst();
        comboContainer.getChildren().add(comboBox);
        comboContainer.setAlignment(Pos.CENTER);
        digButton = new Button();
        dropButton = new Button();
        digButton.setDisable(true);
        dropButton.setDisable(true);
        digButton.setText("Dig");
        digButton.setStyle("-fx-background-color: linear-gradient" +
                "(#3e5ca4, #263868); -fx-text-fill:" +
                " #FFFFFF; -fx-border-color: #8aa8e5; -fx-font-size: 12px");
        dropButton.setText("Drop");
        dropButton.setStyle("-fx-background-color: linear-gradient" +
                "(#3e5ca4, #263868); -fx-text-fill:" +
                " #FFFFFF; -fx-border-color: #8aa8e5; -fx-font-size: 12px");
        dropIndex = new TextField();
        dropIndex.setDisable(true);
        dropIndex.setPrefColumnCount(10);
        dropIndex.setPromptText("Drop Index");
        HBox dropSection = new HBox();
        dropSection.getChildren().addAll(dropButton, dropIndex);
        dropSection.setSpacing(5);
        rightBox.getChildren().addAll(moveGrid, comboContainer, digButton,
                dropSection);
        Insets insets = new Insets(70, 40, 0, 100);
        rightBox.setPadding(insets);
        rightBox.setSpacing(20);
    }

    /**
     * Draw a single tile, including rectangle, exits and number of blocks
     * Call drawExit method to draw all the exits for each tile
     *
     * @param position :   The position of the current tile of builder
     * @param tile     :   The current tile of builder
     */
    public void drawTile(Position position, Tile tile) {
        Color color = null;
        if (!tile.getBlocks().isEmpty()) {
            String blockType = null;
            try {
                blockType = tile.getTopBlock().getBlockType();
            } catch (TooLowException e) {
                e.printStackTrace();
            }
            switch (blockType) {
                case "grass":
                    color = Color.GREEN;
                    break;
                case "wood":
                    color = Color.BROWN;
                    break;
                case "soil":
                    color = Color.BLACK;
                    break;
                case "stone":
                    color = Color.GREY;
                    break;
            }
            context.setFill(color);
            context.fillRect(position.getX() * 40, position.getY() * 40,
                    REC_LENGTH, REC_LENGTH);
        }
        context.setStroke(Color.BLACK);
        context.strokeRect(position.getX() * 40, position.getY() * 40,
                REC_LENGTH, REC_LENGTH);
        context.setFill(Color.WHITE);
        context.fillText(String.valueOf(tile.getBlocks().size()), position
                .getX() * 40 + 16, position.getY() * 40 + 27);
        if (tile.getExits().containsKey("north")) {
            drawExit("north", position);
        }
        if (tile.getExits().containsKey("west")) {
            drawExit("west", position);
        }
        if (tile.getExits().containsKey("south")) {
            drawExit("south", position);
        }
        if (tile.getExits().containsKey("east")) {
            drawExit("east", position);
        }

    }

    /**
     * Draw the single builder, which is represented by a yellow circle
     *
     * @param newPos :   The position that builder will stay, which will
     *               always be the centre of the canvas.
     */
    public void drawBuilder(Position newPos) {
        context.setFill(Color.YELLOW);
        context.fillOval(newPos.getX() * 40 + 16, newPos.getY() * 40 + 6, 8,
                8);
        context.strokeOval(newPos.getX() * 40 + 16, newPos.getY() * 40 + 6,
                8, 8);
    }

    /**
     * Draw a single exit for different direction
     * The exit will be represented by a white triangle
     *
     * @param direction :   which direction that this tile contains a exit
     * @param position  :   The position of each tile
     *                  Draw the exits for all of the tiles
     */
    public void drawExit(String direction, Position position) {
        double point1X = 0;
        double point2X = 0;
        double point3X = 0;
        double point1Y = 0;
        double point2Y = 0;
        double point3Y = 0;
        switch (direction) {
            case "north":
                point1X = position.getX() * 40 + 20;
                point2X = position.getX() * 40 + 16;
                point3X = position.getX() * 40 + 24;
                point1Y = position.getY() * 40;
                point2Y = position.getY() * 40 + 5;
                point3Y = position.getY() * 40 + 5;
                break;
            case "west":
                point1X = position.getX() * 40;
                point2X = position.getX() * 40 + 5;
                point3X = position.getX() * 40 + 5;
                point1Y = position.getY() * 40 + 20;
                point2Y = position.getY() * 40 + 25;
                point3Y = position.getY() * 40 + 15;
                break;
            case "south":
                point1X = position.getX() * 40 + 20;
                point2X = position.getX() * 40 + 16;
                point3X = position.getX() * 40 + 24;
                point1Y = position.getY() * 40 + 40;
                point2Y = position.getY() * 40 + 35;
                point3Y = position.getY() * 40 + 35;
                break;
            case "east":
                point1X = position.getX() * 40 + 40;
                point2X = position.getX() * 40 + 35;
                point3X = position.getX() * 40 + 35;
                point1Y = position.getY() * 40 + 20;
                point2Y = position.getY() * 40 + 25;
                point3Y = position.getY() * 40 + 15;
        }

        double[] positionX = {point1X, point2X, point3X};
        double[] positionY = {point1Y, point2Y, point3Y};
        context.setStroke(Color.BLACK);
        context.strokePolygon(positionX, positionY, 3);
        context.setFill(Color.WHITE);
        context.fillPolygon(positionX, positionY, 3);
    }

    /**
     * Draw all the content of world map, including tiles and builder
     * It is also a update method which will be called in controller
     * Draw this map basic on builder and its position
     */
    public void drawMap() {
        context.clearRect(0, 0, 360, 360);
        sta = new SparseTileArray();
        try {
            sta.addLinkedTiles(builder.getCurrentTile(), startPosition
                            .getX(),
                    startPosition.getY());
        } catch (WorldMapInconsistentException e) {
            e.printStackTrace();
        }
        int xGap = 4 - startPosition.getX();
        int yGap = 4 - startPosition.getY();
        for (int i = -sta.getTiles().size(); i < sta.getTiles().size(); i++) {
            for (int j = -sta.getTiles().size(); j < sta.getTiles().size();
                 j++) {
                Position position = new Position(i, j);
                if (sta.getTile(position) != null) {
                    Position newPos = new Position(position.getX() + xGap,
                            position.getY() + yGap);
                    drawTile(newPos, sta.getTile(position));
                    if (sta.getTile(position) == builder.getCurrentTile()) {
                        drawBuilder(newPos);
                    }
                }
            }
        }
    }

    /**
     * add button handler to each button
     *
     * @param handler :   The handler of all the buttons
     */
    public void addButtonHandler(EventHandler<ActionEvent> handler) {
        for (Button b : moveButtons) {
            b.setOnAction(handler);
        }
        digButton.setOnAction(handler);
        dropButton.setOnAction(handler);
    }

    /**
     * Get the combo Box, which will spawn builder and block
     *
     * @return :   the comboBox of move section
     */
    public ComboBox getComboBox() {
        return comboBox;
    }

    /**
     * Get the index that player have entered to drop block
     *
     * @return :   the dropIndex text field
     */
    public TextField getTextIndex() {
        return dropIndex;
    }

    /**
     * Get the root box as the scene of this view
     *
     * @return :   a new scene, which contains all the components of this
     * application
     */
    public Scene getScene() {
        return new Scene(rootBox);
    }


}
