package com.currenjin.shapes;

import java.awt.*;

public class BaseShape implements Shape {
    private int x;
    private int y;
    private Color color;
    private boolean selected = false;

    public BaseShape(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public boolean isInsideBounds(int x, int y) {
        return false;
    }

    @Override
    public void select() {
        this.selected = true;
    }

    @Override
    public void unSelect() {
        this.selected = false;
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void paint(Graphics graphics) {
        // TODO: paint for graphics
    }
}
