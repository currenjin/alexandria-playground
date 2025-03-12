package com.currenjin.graphic.diagram;

public class Circle extends Dot {
    private int radius = 0;

    public Circle(int x, int y, int radius) {
        super(x, y);
        this.radius = radius;
    }

    @Override
    public void draw() {
        // TODO: 원을 그린다.
    }
}
