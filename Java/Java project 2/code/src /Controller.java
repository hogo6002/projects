import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;

import java.util.Optional;

public class Controller {

    private View view;

    public Controller(View view) {
        this.view = view;
        view.addButtonHandler(new DirHandler());
    }

    private class DirHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Button pressedButton = (Button) event.getSource();
            String direction = pressedButton.getText();
            if (direction == "North" || direction == "South" || direction == "East" || direction == "West") {
                Boolean flage = false;
                Boolean leave = true;
                for (String s : view.cartographer.getCurrentRoom().getExits().keySet()) {
                    for (Thing t : view.cartographer.getCurrentRoom().getContents()) {
                        if (t instanceof Critter && ((Critter) t).isAlive()) {
                            leave = false;
                        }
                    }
                    if (direction.equals(s)) {
                        if (leave) {
                            view.cartographer.getCurrentRoom().leave(view.cartographer.getPlayer());
                            view.cartographer.setCurrentRoom(view.cartographer.getCurrentRoom().getExits().get(direction));
                            view.cartographer.getCurrentRoom().enter(view.cartographer.getPlayer());
                            view.move();
                            view.message.appendText("\nYou enter " + view.cartographer.getCurrentRoom().getDescription());
                        }
                        flage = true;
                    }
                }

                if (!flage) {
                    view.message.appendText("\nNo door that way");
                } else if (!leave) {
                    view.message.appendText("\nSomething prevents you from leaving");
                }

            } else if (pressedButton.getText().equals("Look")) {
                Room room = view.cartographer.getCurrentRoom();
                Explorer player = view.cartographer.getPlayer();
                String str = "\n" + room.getDescription() + " - you see:";
                for (Thing t : room.getContents()) {
                    str += "\n  " + t.getShortDescription();
                }
                double value = 0.0;
                for (Thing t : player.getContents()) {
                    if (t instanceof Treasure) {
                        Treasure t1 = (Treasure) t;
                        value += t1.getValue();
                    }
                }
                str += "\nYour are carrying:\n" + "worth " + value + " in total";
                view.message.appendText(str);
            } else if (pressedButton.getText().equals("Examine")) {
                checkWindow();
            } else if (pressedButton.getText().equals("Drop")) {
                dropWindow();
            } else if (pressedButton.getText().equals("Take")) {
                takeWindow();
            } else if (pressedButton.getText().equals("Fight")) {
                fightWindow();
            } else if (pressedButton.getText().equals("Save")) {
                saveWindow();
            }
        }

        public void checkWindow() {
            TextInputDialog te = new TextInputDialog();
            te.initStyle(StageStyle.UNIFIED);
            te.setTitle("Examine what?");
            te.setHeaderText(null);
            te.setGraphic(null);
            Optional<String> res = te.showAndWait();
            boolean flag = false;
            if (res.isPresent()) {
                String check = te.getResult();
                for (Thing t : view.cartographer.getPlayer().getContents()) {
                    if (check.equals(t.getShortDescription())) {
                        view.message.appendText("\n" + t.getLong());
                        flag = true;
                        break;
                    }
                }
                if (flag == false) {
                    for (Thing t1 : view.cartographer.getCurrentRoom().getContents()) {
                        if (check.equals(t1.getShortDescription())) {
                            view.message.appendText("\n" + t1.getLong());
                            flag = true;
                            break;
                        }
                    }
                }
                if (flag == false) {
                    view.message.appendText("\nNothing found with" + check);
                }
            }

        }

        public void dropWindow() {
            TextInputDialog te = new TextInputDialog();
            te.initStyle(StageStyle.UNIFIED);
            te.setTitle("Item to dorp?");
            te.setHeaderText(null);
            te.setGraphic(null);
            Optional<String> res = te.showAndWait();
            boolean flag = false;
            if (res.isPresent()) {
                String drop = te.getResult();
                for (Thing t : view.cartographer.getPlayer().getContents()) {
                    if (drop.equals(t.getShortDescription())) {
                        view.cartographer.getPlayer().drop(t);
                        view.cartographer.getCurrentRoom().enter(t);
                        flag = true;
                        view.cartographer.reDraw();
                    }
                }
                if (!flag) {
                    view.message.appendText("\nNothing found with " + drop);
                }
            }
        }

        public void takeWindow() {
            TextInputDialog te = new TextInputDialog();
            te.initStyle(StageStyle.UNIFIED);
            te.setTitle("Take what?");
            te.setHeaderText(null);
            te.setGraphic(null);
            Optional<String> res = te.showAndWait();
            boolean flag = false;
            if (res.isPresent()) {
                String take = te.getResult();
                for (Thing t : view.cartographer.getCurrentRoom().getContents()) {
                    if (take.equals(t.getShortDescription())) {
                        if (!(t instanceof Mob)) {
                            view.cartographer.getCurrentRoom().leave(t);
                            view.cartographer.getPlayer().add(t);
                            flag = true;
                            view.cartographer.reDraw();
                        }
                    }
                }
                if (!flag) {
                    view.message.appendText("\nNothing found with " + take);
                }
            }
        }

        public void fightWindow() {
            TextInputDialog te = new TextInputDialog();
            te.initStyle(StageStyle.UNIFIED);
            te.setTitle("Fight what?");
            te.setHeaderText(null);
            te.setGraphic(null);
            Optional<String> res = te.showAndWait();
            if (res.isPresent()) {
                String fight = te.getResult();
                boolean found = false;
                boolean win = false;
                for (Thing t : view.cartographer.getCurrentRoom().getContents()) {
                    if (fight.equals(t.getShortDescription())) {
                        if (t instanceof Critter && ((Critter) t).isAlive()) {
                            found = true;
                            view.cartographer.getPlayer().fight((Critter)t);
                            if (view.cartographer.getPlayer().isAlive()) {
                                win = true;
                                ((Critter) t).setAlive(false);
                                break;
                            }
                        }
                    }
                }
                if (!found) {
                }else if (win) {
                    view.message.appendText("\nYou won");
                } else {
                    view.cartographer.getPlayer().setAlive(false);
                    view.message.appendText("\nGame Over");
                    for (Button b : view.dirButtons) {
                        System.out.print(123);
                        b.setDisable(true);
                    }
                    for (Button b : view.funButtons) {
                        b.setDisable(true);
                    }
                }
            }
        }

        public void saveWindow() {
            TextInputDialog te = new TextInputDialog();
            te.initStyle(StageStyle.UNIFIED);
            te.setTitle("Save filename?");
            te.setHeaderText(null);
            te.setGraphic(null);
            Optional<String> res = te.showAndWait();
            if (res.isPresent()) {
                String filename = te.getResult();
                if (MapIO.saveMap(view.cartographer.getCurrentRoom(), filename)) {
                    MapIO.saveMap(view.cartographer.getCurrentRoom(), filename);
                    view.message.appendText("\nSaved");
                } else {
                    view.message.appendText("\nUnable to save");
                }
            }
        }
    }
}
