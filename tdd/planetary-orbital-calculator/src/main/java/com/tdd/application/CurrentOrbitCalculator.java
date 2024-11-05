package com.tdd.application;

import com.tdd.domain.Orbit;
import com.tdd.domain.PlanetOrbit;
import com.tdd.util.JulianClock;

public class CurrentOrbitCalculator {

    public static Orbit of(PlanetOrbit planetOrbit) {

        return Orbit.of(getCurrentOrbitElement(planetOrbit.getLongRadius(), planetOrbit.getChangePerCentury().getLongRadius()),
                getCurrentOrbitElement(planetOrbit.getEccentricity(), planetOrbit.getChangePerCentury().getEccentricity()),
                getCurrentOrbitElement(planetOrbit.getInclination(), planetOrbit.getChangePerCentury().getInclination()),
                getCurrentOrbitElement(planetOrbit.getLongitudeOfAscendingNode(), planetOrbit.getChangePerCentury().getLongitudeOfAscendingNode()),
                getCurrentOrbitElement(planetOrbit.getAverageLongitude(), planetOrbit.getChangePerCentury().getAverageLongitude()),
                getCurrentOrbitElement(planetOrbit.getPerihelionLongitude(), planetOrbit.getChangePerCentury().getPerihelionLongitude()));
    }

    private static double getCurrentOrbitElement(Double element, Double changePerCenturyByElement) {
        double elapsedCentury = JulianClock.elapsedCentury();

        return element + (changePerCenturyByElement * elapsedCentury);
    }
}
