package com.tdd.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KeplerThirdLawTest {

    public static final double DELTA = 0.01;
    public static final double CENTRAL_MASS = 1.0;

    @ParameterizedTest
    @CsvSource({
        "0.39, 0.24",   // Mercury
        "0.72, 0.62",   // Venus
        "1.0, 1.0",     // Earth
        "1.52, 1.88",   // Mars
        "5.20, 11.86",  // Jupiter
        "9.54, 29.46",  // Saturn
    })
    public void testCalculateOrbitalPeriodAllPlanet(double semiMajorAxis, double expectedPeriod) {
        double actualPeriod = KeplerThirdLaw.calculateOrbitalPeriod(semiMajorAxis, CENTRAL_MASS);

        assertEquals(expectedPeriod, actualPeriod, DELTA);
    }

    @Test
    public void testInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> KeplerThirdLaw.calculateOrbitalPeriod(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> KeplerThirdLaw.calculateOrbitalPeriod(1, -1));
        assertThrows(IllegalArgumentException.class, () -> KeplerThirdLaw.calculateOrbitalPeriod(0, 1));
        assertThrows(IllegalArgumentException.class, () -> KeplerThirdLaw.calculateOrbitalPeriod(1, 0));
    }
}