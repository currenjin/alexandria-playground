package com.tdd.domain;

public class EclipticCoordinate {

    public static EclipticCoordinate of(double x, double y) {
        return new EclipticCoordinate(x, y);
    }

    private final double x;
    private final double y;

    private EclipticCoordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
