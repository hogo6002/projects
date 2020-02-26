package GUI;

import Data.Item;
import Data.StoreInfos;
import Data.Transaction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


import java.time.LocalDate;
import java.util.*;

public class View {
    private BorderPane rootBox;
    StoreInfos storeInfos;
    private Set<Button> allBtns;
    TextField input;
    List<TextField> transText;
    List<TextField> itemText;
    TextArea commentInput;
    Alert alert;
    Alert wrongAlert;
    Item tranItem;

    public View() {
        rootBox = new BorderPane();
        rootBox.setPadding(new Insets(50, 30, 20, 30));
        allBtns = new HashSet<>();
        setLeft();
        storeInfos = new StoreInfos();
        addRight(1, null);
    }

    public void addRight(int type, Item item) {
        if (type == 1) {
            setViewScreen();
        } else if (type == 2) {
            setItemViewScreen(item);
        } else if (type == 3) {
            setNewTranScreen(item);
        } else if (type == 4) {
            setNewItemScreen();
        }
    }

    public void addButtonHandler(
            EventHandler<ActionEvent> handler) {
        for (Button b : allBtns) {
            b.setOnAction(handler);
        }
    }

    public void setLeft() {
        VBox vBox = new VBox();
        Button viewBtn = new Button("查看所有");
        List<Button> leftBtns = new ArrayList<>();
        Button addBtn = new Button("添加产品");
        Button createBtn = new Button("新建交易");
        leftBtns.add(viewBtn);
        leftBtns.add(addBtn);
        leftBtns.add(createBtn);
        allBtns.addAll(leftBtns);
        for (Button button : leftBtns) {
            button.setPrefWidth(150);
            button.setPrefHeight(70);
            button.setFont(new Font(19));
        }
        vBox.getChildren().addAll(viewBtn, addBtn, createBtn);
        vBox.setMinWidth(200);
        vBox.setSpacing(50);
        rootBox.setLeft(vBox);
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText("变更提示");
        alert.setContentText("恭喜，添加成功!");
        wrongAlert = new Alert(Alert.AlertType.ERROR);
        wrongAlert.setTitle("错误提示");
        wrongAlert.setHeaderText("添加失败");
        wrongAlert.setContentText("请检查各项输入!");
    }

    public void setViewScreen() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        input = new TextField();
        input.setPromptText("输入产品名或编号");
        input.setPrefColumnCount(20);
        input.setPrefHeight(40);
        Button searchBtn = new Button("搜索");
        allBtns.add(searchBtn);
        searchBtn.setPrefHeight(40);
        searchBtn.setPrefWidth(100);
        hBox.getChildren().addAll(input, searchBtn);
        hBox.setMinHeight(80);
        hBox.setSpacing(30);
        TextArea itemsInfo = new TextArea();
        itemsInfo.setEditable(false);
        for (Item item : storeInfos.getAllItems()) {
            if (item.lowStock()) {
                itemsInfo.appendText("低货存： ");
            }
            itemsInfo.appendText(item + "\n\n");
        }
        itemsInfo.setPrefHeight(400);
        itemsInfo.setPrefWidth(600);
        borderPane.setTop(hBox);
        borderPane.setBottom(itemsInfo);
        borderPane.setMinHeight(200);
        rootBox.setCenter(borderPane);
    }

    public void setItemViewScreen(Item item) {
        BorderPane borderPane = new BorderPane();
        VBox top = new VBox();
        tranItem = item;
        Label label = new Label(item.getId() + " - " +  item.getName());
        label.setFont(new Font(22));
        HBox stockBox = new HBox();
        stockBox.setPadding(new Insets(0, 0, 0, 30));
        Label stock = new Label("库存： " +  item.getStock());
        stock.setFont(new Font(18));
        stock.setPrefHeight(40);
        Button changeBtn = new Button("修改库存");
        changeBtn.setPrefHeight(30);
        changeBtn.setPrefWidth(100);
        changeBtn.setFont(new Font(18));
        stockBox.getChildren().addAll(stock, changeBtn);
        allBtns.add(changeBtn);
        stockBox.setSpacing(50);
        top.getChildren().addAll(label, stockBox);
        top.setSpacing(10);
        borderPane.setTop(top);
        TextArea transInfo = new TextArea();
        transInfo.setEditable(false);
        for (Transaction transaction : storeInfos.getItemTrans(item.getId())) {
            if (transaction.getChangeNum() < 0) {
                transInfo.appendText(" - ");
            } else {
                transInfo.appendText(" + ");
            }
            transInfo.appendText(transaction + "\n\n");
        }
        transInfo.setPrefHeight(400);
        transInfo.setPrefWidth(600);
        borderPane.setBottom(transInfo);
        rootBox.setCenter(borderPane);
    }

    public void setNewTranScreen(Item item) {
        VBox vBox = new VBox();
        List<Label> labels = new ArrayList<>();
        transText = new ArrayList<>();

        HBox dateInfo = new HBox();
        Label dateLabel = new Label("日期");
        labels.add(dateLabel);
        HBox date = new HBox();
        TextField dateYear = new TextField(LocalDate.now().getYear() + "");
        Label dateSplit1 = new Label(" - ");
        TextField dateMonth =
                new TextField(LocalDate.now().getMonthValue() + "");
        Label dateSplit2 = new Label(" - ");
        TextField dateDay = new TextField(LocalDate.now().getDayOfMonth() + "");
        dateYear.setPrefColumnCount(4);
        dateMonth.setPrefColumnCount(2);
        dateDay.setPrefColumnCount(2);
        transText.add(dateYear);
        transText.add(dateMonth);
        transText.add(dateDay);
        labels.add(dateSplit1);
        labels.add(dateSplit2);
        date.getChildren().addAll(dateYear, dateSplit1,
                dateMonth, dateSplit2, dateDay);
        dateInfo.getChildren().addAll(dateLabel, date);
        dateInfo.setSpacing(20);

        HBox itemInfo = new HBox();
        Label itemLabel = new Label("产品名或编号:");
        TextField itemInput;
        if (item != null) {
            itemInput = new TextField(item.getName());
        } else {
            itemInput = new TextField();
        }
        transText.add(itemInput);
        labels.add(itemLabel);
        itemInfo.getChildren().addAll(itemLabel, itemInput);
        itemInfo.setSpacing(20);

        HBox adminInfo = new HBox();
        Label adminLabel = new Label("员工名或编号:");
        TextField adminInput = new TextField();
        transText.add(adminInput);
        labels.add(adminLabel);
        adminInfo.getChildren().addAll(adminLabel, adminInput);
        adminInfo.setSpacing(20);

        HBox stockInfo = new HBox();
        Label stockLabel = new Label("增/减数量:");
        TextField stockInput = new TextField();
        stockInput.setPrefColumnCount(5);
        transText.add(stockInput);
        labels.add(stockLabel);
        stockInfo.getChildren().addAll(stockLabel, stockInput);
        stockInfo.setSpacing(20);

        HBox commentInfo = new HBox();
        Label commentLabel = new Label("备注:");
        commentInput = new TextArea("");
        commentInput.setPromptText("可不填");
        commentInput.setPrefColumnCount(50);
        commentInput.setFont(new Font(16));
        commentLabel.setMinWidth(40);
        labels.add(commentLabel);
        commentInfo.getChildren().addAll(commentLabel, commentInput);
        commentInfo.setSpacing(20);

        BorderPane borderPane = new BorderPane();
        Button submitBtn = new Button("添加变更");
        submitBtn.setFont(new Font(16));
        submitBtn.setPadding(new Insets(10));
        allBtns.add(submitBtn);
        borderPane.setCenter(submitBtn);


        for (Label label : labels) {
            label.setPrefHeight(33);
            label.setFont(new Font(16));
            if (!label.getText().equals(" - ")) {
                label.setMinWidth(100);
            }
        }
        for (TextField input : transText) {
            input.setFont(new Font(16));
            input.setPrefHeight(33);
        }
        vBox.getChildren().addAll(dateInfo, itemInfo, adminInfo, stockInfo,
                commentInfo, borderPane);
        vBox.setSpacing(20);
        rootBox.setCenter(vBox);
    }

    public void setNewItemScreen() {
        VBox vBox = new VBox();
        List<Label> labels = new ArrayList<>();
        itemText = new ArrayList<>();
        HBox itemInfo = new HBox();
        Label itemLabel = new Label("请输入产品名： ");
        TextField itemInput = new TextField();
        itemText.add(itemInput);
        labels.add(itemLabel);
        itemInfo.getChildren().addAll(itemLabel, itemInput);

        HBox stockInfo = new HBox();
        Label stockLabel = new Label("请输入产品库存： ");
        TextField stockInput = new TextField();
        stockInput.setPrefColumnCount(5);
        itemText.add(stockInput);
        labels.add(stockLabel);
        stockInfo.getChildren().addAll(stockLabel, stockInput);

        BorderPane borderPane = new BorderPane();
        Button submitBtn = new Button("确定添加");
        submitBtn.setFont(new Font(16));
        submitBtn.setPadding(new Insets(10));
        allBtns.add(submitBtn);
        borderPane.setCenter(submitBtn);


        for (Label label : labels) {
            label.setPrefHeight(33);
            label.setFont(new Font(16));
            label.setMinWidth(150);
        }
        for (TextField input : itemText) {
            input.setFont(new Font(16));
            input.setPrefHeight(33);
        }

        vBox.getChildren().addAll(itemInfo, stockInfo, borderPane);
        vBox.setSpacing(40);
        vBox.setPadding(new Insets(10, 0, 0, 0));
        rootBox.setCenter(vBox);
    }


    public Scene getScene() {
        return new Scene(rootBox, 850, 600);
    }
}
