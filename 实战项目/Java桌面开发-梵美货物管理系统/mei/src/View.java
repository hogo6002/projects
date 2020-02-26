import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.Optional;

public class View {
    public Button[] funButtons;
    private BorderPane rootBox;
    public TextArea message;
    public Store store;
    public TextField proName;
    public TextField proName1;
    private TextArea bottom;
    private BorderPane top;
    public TextField proStock;


    /**
     * Constructor
     */
    public View() {
        rootBox = new BorderPane();
        store = new Store();
        bottom = new TextArea();
        top = new BorderPane();
        addComponents();
    }


    public void addTopComponents(BorderPane top) {
        HBox addNewPro = addNewPro();
        HBox changeStock = changeStock();
        top.setTop(addNewPro);
        top.setCenter(changeStock);
    }

    public void addButtonHandler(EventHandler<ActionEvent> handler) {
        for (Button b : funButtons) {
            b.setOnAction(handler);
        }
    }

    public HBox addNewPro() {
        HBox addNewPro = new HBox();
        Label nawPro = new Label("新加产品");
        proName = new TextField();
        proName.setDisable(false);
        proName.setPrefColumnCount(10);
        proName.setPromptText("输入新产品名字");
        Button addBtn = new Button("添加");
        addNewPro.getChildren().addAll(nawPro, proName, addBtn);
        funButtons[0] = addBtn;
        return addNewPro;
    }

    public HBox changeStock() {
        HBox changeStock = new HBox();
        Label nameLabel = new Label("产品名");
        proName1 = new TextField();
        proName1.setDisable(false);
        proName1.setPrefColumnCount(10);
        proName1.setPromptText("输入产品名字");
        proStock = new TextField();
        proStock.setPrefColumnCount(5);
        proStock.setPromptText("变化数量");
        Button addBtn = new Button("改变数量");
        funButtons[1] = addBtn;
        changeStock.getChildren().addAll(nameLabel, proName1, proStock,
                addBtn);
        return changeStock;
    }

    public TextArea addBottomComponents() {
        message = new TextArea();
        message.appendText("产品名及数量： \n");
        for (Product product : store.productList()) {
            message.appendText("名字：" + product.getName() + "，数量：" + product.getStock() + "\n");
        }
        return message;
    }

    public void addComponents() {
        funButtons = new Button[2];
        addTopComponents(top);
        bottom = addBottomComponents();
        rootBox.setTop(top);
        rootBox.setBottom(bottom);
    }

    /*public void move() {
        cartographer.reDraw();
    }*/


    public Scene getScene() {
        return new Scene(rootBox);
    }

}
