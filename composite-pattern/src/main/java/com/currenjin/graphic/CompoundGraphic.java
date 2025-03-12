package com.currenjin.graphic;

import java.util.List;

public class CompoundGraphic implements Graphic {
    private List<Graphic> graphics;

    public void add(Graphic graphic) {
        this.graphics.add(graphic);
    }

    public void remove(Graphic graphic) {
        this.graphics.remove(graphic);
    }

    @Override
    public void move(int x, int y) {
        this.graphics.forEach(graphic -> graphic.move(x, y));
    }

    @Override
    public void draw() {
        this.graphics.forEach(Graphic::draw);
    }
}
