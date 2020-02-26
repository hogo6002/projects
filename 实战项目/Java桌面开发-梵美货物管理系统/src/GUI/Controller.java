package GUI;

import Data.Admin;
import Data.EasyDate;
import Data.Item;
import Data.StoreInfos;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;




public class Controller {
    private View view;

    public Controller(View view) {
        this.view = view;
        view.addButtonHandler(new ButtonHandler());
    }

    private class ButtonHandler implements EventHandler<ActionEvent> {
        /**
         * Invoked when a specific event of the type for which this handler is
         * registered happens.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(ActionEvent event) {
            Button pressedBtn = (Button) event.getSource();
            if (pressedBtn.getText().equals("搜索")) {
                goToItemPage();
            } else if (pressedBtn.getText().equals("查看所有")) {
                goToViewPage();
            } else if (pressedBtn.getText().equals("添加变更")) {
                submitTransPage();
            } else if (pressedBtn.getText().equals("新建交易")) {
                goToTransPage(null);
            } else if (pressedBtn.getText().equals("修改库存")) {
                goToTransPage(view.tranItem);
            } else if (pressedBtn.getText().equals("添加产品")) {
                goToAddItemPage();
            } else if (pressedBtn.getText().equals("确定添加")) {
                submitItem();
            }
        }

        public void goToItemPage() {
            String input = view.input.getText();
            Item item = null;
            try {
                int id = Integer.parseInt(input);
                if (view.storeInfos.getItem(id) != null) {
                    item = view.storeInfos.getItem(id);
                }
            } catch (Exception e) {
                for (Item temp : view.storeInfos.getAllItems()) {
                    if (input.equals(temp.getName())) {
                        item = temp;
                        break;
                    }
                }
            }
            if (item != null) {
                view.addRight(2, item);
            }
            view.addButtonHandler(new ButtonHandler());
        }

        public void goToViewPage() {
            view.addRight(1, null);
            view.addButtonHandler(new ButtonHandler());
        }

        public void goToTransPage(Item item) {
            view.addRight(3, item);
            view.addButtonHandler(new ButtonHandler());
        }

        public void submitTransPage() {
            try {
                int year = Integer.parseInt(view.transText.get(0).getText());
                int month = Integer.parseInt(view.transText.get(1).getText());
                int day = Integer.parseInt(view.transText.get(2).getText());
                EasyDate date = new EasyDate(year, month, day);
                Item item = null;
                try {
                    int id = Integer.parseInt(view.transText.get(3).getText());
                    if (view.storeInfos.getItem(id) != null) {
                        item = view.storeInfos.getItem(id);
                    }
                } catch (Exception e) {
                    for (Item temp : view.storeInfos.getAllItems()) {
                        if (view.transText.get(3).getText().equals(temp.getName())) {
                            item = temp;
                            break;
                        }
                    }
                }
                Admin admin = null;
                try {
                    int id = Integer.parseInt(view.transText.get(4).getText());
                    if (view.storeInfos.getAdmin(id) != null) {
                        admin = view.storeInfos.getAdmin(id);
                    }
                } catch (Exception e) {
                    for (Admin temp : view.storeInfos.getAllAdmins()) {
                        if (view.transText.get(4).getText().equals(temp.getName())) {
                            admin = temp;
                            break;
                        }
                    }
                }
                int number =
                        Integer.parseInt(view.transText.get(5).getText());

                String comment = view.commentInput.getText();
                if (view.storeInfos.createTrans(date, item.getId(),
                        admin.getId(), number, comment)) {
                    goToViewPage();
                    view.alert.showAndWait();
                } else {
                    view.wrongAlert.showAndWait();
                }
            } catch (Exception e) {
                view.wrongAlert.showAndWait();
            }
            view.addButtonHandler(new ButtonHandler());
        }

        public void goToAddItemPage() {
            view.addRight(4, null);
            view.addButtonHandler(new ButtonHandler());
        }

        public void submitItem() {
            try {
                String name = view.itemText.get(0).getText();
                int stock = Integer.parseInt(view.itemText.get(1).getText());
                if (view.storeInfos.addItem(name, stock)) {
                    goToViewPage();
                    view.alert.showAndWait();
                } else {
                    view.wrongAlert.showAndWait();
                }
            } catch (Exception e) {
                view.wrongAlert.showAndWait();
            }

        }

    }
}
