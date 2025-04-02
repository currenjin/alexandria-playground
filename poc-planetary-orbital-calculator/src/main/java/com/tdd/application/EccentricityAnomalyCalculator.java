package com.tdd.application;

public class EccentricityAnomalyCalculator {

    public static double calculate(Double eccentricity, Double averageLongitude, Double perihelionLongitude) {
        validate(eccentricity, averageLongitude, perihelionLongitude);

        double meanAnomaly = calculateMeanAnomaly(averageLongitude, perihelionLongitude);
        double radianMeanAnomaly = meanAnomaly * (Math.PI / 180);

        return (radianMeanAnomaly + (eccentricity * Math.sin(radianMeanAnomaly))) / (1 - (eccentricity * Math.cos(radianMeanAnomaly)));
    }

    public static double calculateMeanAnomaly(Double averageLongitude, Double perihelionLongitude) {
        validateMeanAnomalyCalculator(averageLongitude, perihelionLongitude);

        return averageLongitude - perihelionLongitude;
    }

    private static void validateMeanAnomalyCalculator(Double averageLongitude, Double perihelionLongitude) {
        if (averageLongitude == null) {
            throw new IllegalArgumentException();
        }

        if (perihelionLongitude == null) {
            throw new IllegalArgumentException();
        }
    }

    private static void validate(Double eccentricity, Double averageLongitude, Double perihelionLongitude) {
        if (eccentricity == null) {
            throw new IllegalArgumentException();
        }

        if (averageLongitude == null) {
            throw new IllegalArgumentException();
        }

        if (perihelionLongitude == null) {
            throw new IllegalArgumentException();
        }
    }
}
