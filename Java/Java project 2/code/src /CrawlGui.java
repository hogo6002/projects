import javafx.stage.Stage;

public class CrawlGui extends javafx.application.Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View();
        primaryStage.setScene(view.getScene());
        Controller controller = new Controller(view);
        primaryStage.setTitle("Crawl - Explore");
        primaryStage.show();
    }


}
