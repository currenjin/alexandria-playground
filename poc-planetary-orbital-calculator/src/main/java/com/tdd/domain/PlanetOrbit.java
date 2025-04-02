package com.tdd.domain;

public class PlanetOrbit extends Orbit {

    public static PlanetOrbit of(Double longRadius,
                           Double eccentricity,
                           Double inclination,
                           Double longitudeOfAscendingNode,
                           Double averageLongitude,
                           Double perihelionLongitude,
                           Double au,
                           Orbit changePerCentury) {

        return new PlanetOrbit(longRadius, eccentricity, inclination, longitudeOfAscendingNode, averageLongitude, perihelionLongitude, au, changePerCentury);
    }

    private final Double au;
    private final Orbit changePerCentury;

    private PlanetOrbit(Double longRadius, Double eccentricity, Double inclination, Double longitudeOfAscendingNode, Double averageLongitude, Double perihelionLongitude, Double au, Orbit changePerCentury) {
        super(longRadius, eccentricity, inclination, longitudeOfAscendingNode, averageLongitude, perihelionLongitude);
        this.au = au;
        this.changePerCentury = changePerCentury;
    }

    public Orbit getChangePerCentury() {
        return this.changePerCentury;
    }

    public Double getAu() {
        return this.au;
    }
}
