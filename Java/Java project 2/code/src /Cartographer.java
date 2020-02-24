
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;

import java.util.*;

public class Cartographer extends javafx.scene.canvas.Canvas {

    private Room startRoom;
    private Room currentRoom;
    private Explorer player;
    private BoundsMapper bm;
    private GraphicsContext context;
    private HBox root;
    private int leftTopX;
    private int leftTopY;


    public Cartographer() {
        root = new HBox();
        startRoom = (Room) MapIO.loadMap("demo.map")[1];
        player = (Explorer) MapIO.loadMap("demo.map")[0];
        bm = new BoundsMapper(startRoom);
        startRoom.enter(getPlayer());
        currentRoom = startRoom;
    }

    public Room getStartRoom() {
        return startRoom;
    }

    public Explorer getPlayer() {
        return player;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setStartRoom(Room startRoom) {
        this.startRoom = startRoom;
    }

    public void setPlayer(Explorer player) {
        this.player = player;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void drawRect(Room room) {
        int minX = bm.xMin;
        int minY = bm.yMin;
        leftTopX = 120 + (bm.coords.get(room).x - minX) * 40;
        leftTopY = 40 + (bm.coords.get(room).y - minY) * 40;
        context.strokeRect(leftTopX, leftTopY,  40, 40);
    }

    public void drawExits(String direction) {
        switch (direction) {
            case "North":
                context.strokeLine(leftTopX + 20, leftTopY + 3, leftTopX + 20, leftTopY - 3);
                break;
            case "South":
                context.strokeLine(leftTopX + 20, leftTopY + 43, leftTopX + 20, leftTopY + 37);
                break;
            case "West":
                context.strokeLine(leftTopX - 3, leftTopY, leftTopX + 3, leftTopY);
                break;
            case "East":
                context.strokeLine(leftTopX + 37, leftTopY + 20, leftTopX + 43, leftTopY + 20);
                break;
        }
    }

    public void drawPlayer() {
        context.fillText("@", leftTopX + 2, leftTopY + 10);
    }


    public void drawItems(Room room) {
        List<Thing> items = new ArrayList<>();
        for (int i = 0; i < room.getContents().size(); i++) {
            if (room.getContents().get(i) instanceof Treasure) {
                Treasure t = (Treasure) room.getContents().get(i);
                items.add(t);
            } else if (room.getContents().get(i) instanceof Critter) {
                Critter c = (Critter) room.getContents().get(i);
                items.add(c);
            }
        }
        for (Thing i : items) {
            if (i instanceof Treasure) {
            context.fillText("$", leftTopX + 20, leftTopY +12);
        } else if (i instanceof Critter) {
            context.fillText("M", leftTopX + 2, leftTopY + 25);
        }
        }
    }

    public void drawRooms() {
        bm.walk();
        List<Room> rooms = new ArrayList (bm.coords.keySet());
        rooms.remove(getStartRoom());
        rooms.add(0, startRoom);
        for (Room r : rooms) {
            drawRect(r);
            for (String s : r.getExits().keySet()) {
                drawExits(s);
            }
            if (r == currentRoom) {
                drawPlayer();
            }
            drawItems(r);
        }
    }

    public void reDraw() {
        clearCanvas();
        drawRooms();
    }


    public void clearCanvas() {
        /* Remove all the shapes drawn on the canvas */
        /* use the canvasContext to draw/clear the canvas */
        context.clearRect(0, 0, (bm.xMax - bm.xMin) * 40 + 200, (bm.yMax - bm.yMin) * 40 + 200);
    }


    public HBox getRoot() {
        Canvas canvas = new Canvas((bm.xMax - bm.xMin) * 40 + 200,(bm.yMax - bm.yMin) * 40 + 200);
        context = canvas.getGraphicsContext2D();
        drawRooms();
        root.getChildren().add(canvas);
        return root;
    }

}
