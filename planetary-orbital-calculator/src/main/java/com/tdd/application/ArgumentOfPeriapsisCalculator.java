package com.tdd.application;

public class ArgumentOfPeriapsisCalculator {
    public static double calculate(Double perihelionLongitude, Double longitudeOfAscendingNode) {
        validate(perihelionLongitude, longitudeOfAscendingNode);

        return perihelionLongitude - longitudeOfAscendingNode;
    }

    private static void validate(Double perihelionLongitude, Double longitudeOfAscendingNode) {
        if (perihelionLongitude == null) {
            throw new IllegalArgumentException();
        }

        if (longitudeOfAscendingNode == null) {
            throw new IllegalArgumentException();
        }
    }
}
