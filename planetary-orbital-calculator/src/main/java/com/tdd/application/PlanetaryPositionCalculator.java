package com.tdd.application;

import com.tdd.domain.EclipticCoordinate;
import com.tdd.domain.Orbit;
import com.tdd.domain.PlanetOrbit;
import com.tdd.domain.PlanetaryPosition;

public class PlanetaryPositionCalculator {

    public static PlanetaryPosition position(PlanetOrbit planet) {

        validatePlanetOrbit(planet);

        EclipticCoordinate eclipticCoordinate = calculateEclipticCoordinate(planet);

        double euclideanDistance = calculateEuclideanDistance(eclipticCoordinate, planet.getAu());
        double trueAnomaly = calculateTrueAnomaly(eclipticCoordinate);

        return PlanetaryPosition.of(euclideanDistance, trueAnomaly);
    }

    private static double calculateTrueAnomaly(EclipticCoordinate eclipticCoordinate) {
        return Math.atan2(eclipticCoordinate.getY(), eclipticCoordinate.getX()) * (180 / Math.PI);
    }

    private static double calculateEuclideanDistance(EclipticCoordinate eclipticCoordinate, double au) {
        return Math.sqrt(Math.pow(eclipticCoordinate.getX(), 2) + Math.pow(eclipticCoordinate.getY(), 2)) / (1000 * au);
    }

    private static EclipticCoordinate calculateEclipticCoordinate(PlanetOrbit planet) {

        Orbit currentOrbit = CurrentOrbitCalculator.of(planet);
        double eccentricityAnomaly = EccentricityAnomalyCalculator.calculate(currentOrbit.getEccentricity(), currentOrbit.getAverageLongitude(), currentOrbit.getPerihelionLongitude());

        double x = (currentOrbit.getLongRadius() * 1000) * (Math.cos(eccentricityAnomaly) - currentOrbit.getEccentricity());
        double y = (currentOrbit.getLongRadius() * 1000) * (Math.sqrt(1 - (currentOrbit.getEccentricity() * currentOrbit.getEccentricity()))) * Math.sin(eccentricityAnomaly);

        return EclipticCoordinate.of(x, y);
    }

    private static void validatePlanetOrbit(PlanetOrbit planet) {
        if (planet == null) {
            throw new IllegalArgumentException();
        }
    }
}
