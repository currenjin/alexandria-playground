package com.currenjin.graphic.diagram;

import com.currenjin.graphic.Graphic;

public class Dot implements Graphic {
    private int x = 0;
    private int y = 0;

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public void draw() {
        // TODO: 점을 그린다.
    }
}
