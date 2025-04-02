package com.tdd.domain;

public class PlanetaryPosition {

    public static PlanetaryPosition of(double euclideanDistance, double trueAnomaly) {
        return new PlanetaryPosition(euclideanDistance, trueAnomaly);
    }

    private final double euclideanDistance;
    private final double trueAnomaly;

    private PlanetaryPosition(double euclideanDistance, double trueAnomaly) {
        this.euclideanDistance = euclideanDistance;
        this.trueAnomaly = trueAnomaly;
    }

    public double getEuclideanDistance() {
        return this.euclideanDistance;
    }

    public double getTrueAnomaly() {
        return this.trueAnomaly;
    }
}
