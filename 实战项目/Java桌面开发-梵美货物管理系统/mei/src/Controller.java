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
            Button pressedButton = (Button) event.getSource();
            if (pressedButton.getText().equals("添加")) {
                Product product = new Product(view.proName.getText());
                view.store.addProduct(product);
            } else if (pressedButton.getText().equals("改变数量")) {
                String name = view.proName1.getText();
                int change = Integer.parseInt(view.proStock.getText());
                Product product;
                if ((product = view.store.getProduct(name)) != null) {
                    product.changeStock(change);
                }
            }
            view.addComponents();
            view.addButtonHandler(new ButtonHandler());
        }
    }
}
