package com.example.vritualbrowser.Entities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.List;

/**
 * Create a view for the map navigation for user to find the destination.
 */
public class MapView extends View {

    // about the style and color information
    Paint paint;

    // the current shopping mall
    Mall mall;

    // To draw something, you need
    Canvas canvas;

    /**
     * Create a map view by the given context. and initial some set.
     * @param context the context that map view draw on.
     */
    public MapView(Context context, String string) {
        super(context);
//        mall = new Mall();
        mall = new Mall(string);
        init();
    }

    public Mall getMall() {
        return mall;
    }

    /**
     * set the color and size to default.
     */
    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(50);
    }


    /**
     * draw the rectangle to represent the store.
     * @param i the index of the store.
     */
    public void drawRect(int i) {
        int left = 200 + mall.getStores().get(i).getPosition().getX() * 100;
        int top = 200 + mall.getStores().get(i).getPosition().getY() * 100;
        canvas.drawRect(left, top,  left + 100, top + 100, paint);
    }

    /**
     * draw the line to represent that user needs to go right, and they may need
     * to change the direction.
     * @param position the current position
     * @param change the next direction
     */
    public void drawRightLine(Position position, String change) {
        int left = 200 + position.getX() * 100;
        int top = 200 + position.getY() * 100;
        paint.setColor(Color.RED);
        if (change.equals("B")) {
            canvas.drawLine(left, top + 50, left + 50, top + 50, paint);
            canvas.drawLine(left + 50, top + 50, left + 50, top + 100, paint);
        } else if (change.equals("T")) {
            canvas.drawLine(left, top + 50, left + 50, top + 50, paint);
            canvas.drawLine(left + 50, top + 50, left + 50, top - 50, paint);
        } else {
            canvas.drawLine(left, top + 50, left + 100, top + 50, paint);
        }

    }

    /**
     * draw the line to represent that user needs to go left, and they may need
     * to change the direction.
     * @param position the current position
     * @param change the next direction
     */
    public void drawLeftLine(Position position, String change) {
        int left = 200 + position.getX() * 100;
        int top = 200 + position.getY() * 100;
        paint.setColor(Color.RED);
        if (change.equals("B")) {
            canvas.drawLine(left + 100, top + 50, left + 50, top + 50, paint);
            canvas.drawLine(left + 50, top + 50, left + 50, top + 100, paint);
        } else if (change.equals("T")) {
            canvas.drawLine(left + 100, top + 50, left + 50, top + 50, paint);
            canvas.drawLine(left + 50, top + 50, left + 50, top, paint);
        } else {
            canvas.drawLine(left + 100, top + 50, left, top + 50, paint);
        }

    }

    /**
     * draw the line to represent that user needs to go down, and they may need
     * to change the direction.
     * @param position the current position
     * @param change the next direction
     */
    public void drawDownLine(Position position, String change) {
        int left = 200 + position.getX() * 100;
        int top = 200 + position.getY() * 100;
        paint.setColor(Color.RED);
        if (change.equals("B")) {
            canvas.drawLine(left + 50, top, left + 50, top + 100, paint);
        } else if (change.equals("R")) {
            canvas.drawLine(left + 50, top, left + 50, top + 50, paint);
            canvas.drawLine(left + 50, top + 50, left + 100, top + 50, paint);
        } else {
            canvas.drawLine(left + 50, top, left + 50, top + 50, paint);
            canvas.drawLine(left + 50, top + 50, left, top + 50, paint);
        }

    }

    /**
     * draw the line to represent that user needs to go top, and they may need
     * to change the direction.
     * @param position the current position
     * @param change the next direction
     */
    public void drawTopLine(Position position, String change) {
        int left = 200 + position.getX() * 100;
        int top = 300 + position.getY() * 100;
        paint.setColor(Color.RED);
        if (change.equals("T")) {
            canvas.drawLine(left + 50, top - 100, left + 50, top, paint);
        } else if (change.equals("R")) {
            canvas.drawLine(left + 50, top - 100, left + 50, top - 150, paint);
            canvas.drawLine(left + 50, top - 150, left + 100, top - 150, paint);
        } else {
            canvas.drawLine(left + 50, top - 50, left + 50, top, paint);
            canvas.drawLine(left + 50, top - 50, left, top - 50, paint);
        }

    }


    /**
     * draw the route which is consisted by multiple lines.
     * @param route the route from beginning to the end.
     */
    public void drawRoute(List<Position> route) {
        int i = 0;
        for (Position pos : route) {
            if (mall.getDirection().get(i).equals("R")) {
                drawRightLine(pos, mall.getDirection().get(i + 1));
            } else if (mall.getDirection().get(i).equals("B")) {
                drawDownLine(pos, mall.getDirection().get(i + 1));
            } else if (mall.getDirection().get(i).equals("L")) {
                drawLeftLine(pos, mall.getDirection().get(i + 1));
            } else {
                drawTopLine(pos, mall.getDirection().get(i + 1));
            }
            i++;
        }
    }

    /**
     * draw the begin store represent as #.
     * @param position the position of this begin store
     */
    public void drawBegin(Position position) {
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        canvas.drawText("#", 200 + (position.getX() * 100) + 40, 200 + (position.getY() * 100) + 50, paint);

    }

    /**
     * draw the destination store represent as #.
     * @param position the position of this destination store
     */
    public void drawDest(Position position) {
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        canvas.drawText("*", 200 + (position.getX() * 100) + 40, 200 + (position.getY() * 100) + 50, paint);
    }

    /**
     * draw the canvas to the map view.
     * @param canvas to show all the content.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        init();
        super.onDraw(canvas);
        this.canvas = canvas;
        for (int i = 0; i < mall.getStores().size(); i++) {
            drawRect(i);
        }
        if (mall.getBegin() != null) {
            drawBegin(mall.getBegin().getPosition());
        }
        if (mall.getEnd() != null) {
            drawDest(mall.getEnd().getPosition());
        }
        if (mall.end != null && mall.begin != null) {
            drawRoute(mall.findRoute());
        }

    }
}
