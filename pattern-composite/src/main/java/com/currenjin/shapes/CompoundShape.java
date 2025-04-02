package com.currenjin.shapes;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompoundShape extends BaseShape {
     private final List<Shape> children = new ArrayList<>();

    public CompoundShape(Shape... shapes) {
        super(0, 0, Color.BLACK);
        add(shapes);
    }

    private void add(Shape... shapes) {
        this.children.addAll(Arrays.asList(shapes));
    }

    @Override
    public int getX() {
        int x = this.children.get(0).getX();
        return x;
    }

    @Override
    public int getY() {
        int y = this.children.get(0).getY();
        return y;
    }
}
