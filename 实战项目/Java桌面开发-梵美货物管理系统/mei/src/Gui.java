import javafx.application.Application;
import javafx.stage.Stage;

public class Gui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        View view = new View();
        primaryStage.setScene(view.getScene());
        Controller controller = new Controller(view);
        primaryStage.setTitle("美容院数据管理");
        primaryStage.show();
    }
}
