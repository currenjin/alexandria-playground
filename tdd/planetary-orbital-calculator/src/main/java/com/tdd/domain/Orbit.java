package com.tdd.domain;

public class Orbit {
    public static Orbit of(Double longRadius,
                           Double eccentricity,
                           Double inclination,
                           Double longitudeOfAscendingNode,
                           Double averageLongitude,
                           Double perihelionLongitude) {
        return new Orbit(longRadius, eccentricity, inclination, longitudeOfAscendingNode, averageLongitude, perihelionLongitude);
    }

    private final Double longRadius;
    private final Double eccentricity;
    private final Double inclination;
    private final Double longitudeOfAscendingNode;
    private final Double averageLongitude;
    private final Double perihelionLongitude;

    protected Orbit(Double longRadius, Double eccentricity, Double inclination, Double longitudeOfAscendingNode, Double averageLongitude, Double perihelionLongitude) {

        this.longRadius = longRadius;
        this.eccentricity = eccentricity;
        this.inclination = inclination;
        this.longitudeOfAscendingNode = longitudeOfAscendingNode;
        this.averageLongitude = averageLongitude;
        this.perihelionLongitude = perihelionLongitude;
    }

    public Double getLongRadius() {
        return longRadius;
    }

    public Double getEccentricity() {
        return eccentricity;
    }

    public Double getInclination() {
        return inclination;
    }

    public Double getLongitudeOfAscendingNode() {
        return longitudeOfAscendingNode;
    }

    public Double getAverageLongitude() {
        return averageLongitude;
    }

    public Double getPerihelionLongitude() {
        return perihelionLongitude;
    }
}
