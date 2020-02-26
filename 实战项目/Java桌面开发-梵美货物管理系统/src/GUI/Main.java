package GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        View view = new View();
        Controller controller = new Controller(view);
        primaryStage.setTitle("梵美货物管理系统");
        primaryStage.setScene(view.getScene());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
