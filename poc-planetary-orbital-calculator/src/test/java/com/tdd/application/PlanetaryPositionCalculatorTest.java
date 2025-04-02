package com.tdd.application;

import com.tdd.domain.PlanetOrbit;
import com.tdd.domain.PlanetaryPosition;
import com.tdd.planet.EARTH;
import com.tdd.util.TimeFreezer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PlanetaryPositionCalculatorTest {

    private static final LocalDateTime EPOCH_TIME = LocalDateTime.of(2000, 1, 1, 12, 0, 0);
    public static final double TRUE_ANOMALY_OF_EPOCH_TIME = -2.600199987674757;
    public static final double EUCLIDEAN_DISTANCE_OF_EPOCH_TIME = 0.9833079767952526;
    public static final double EUCLIDEAN_DISTANCE_OF_A_CENTURY_AFTER_EPOCH_TIME = 1.006907945623497;
    public static final double TRUE_ANOMALY_OF_A_CENTURY_AFTER_EPOCH_TIME = -115.32041792817122;
    public static final double EUCLIDEAN_DISTANCE_OF_A_CENTURY_BEFORE_EPOCH_TIME = 1.004617670114503;
    public static final double TRUE_ANOMALY_OF_A_CENTURY_BEFORE_EPOCH_TIME = 106.92904597271186;

    PlanetOrbit PLANET = PlanetOrbit.of(EARTH.LONG_RADIUS,
            EARTH.ECCENTRICITY,
            EARTH.INCLINATION,
            EARTH.LONGITUDE_OF_ASCENDING_NODE,
            EARTH.AVERAGE_LONGITUDE,
            EARTH.PERIHELION_LONGITUDE,
            EARTH.AU,
            EARTH.CHANGE_PER_CENTURY);

    @AfterEach
    void tearDown() {
        TimeFreezer.rollback();
    }


    @Test
    void 역기점의_행성의_위치를_가져옵니다() {
        TimeFreezer.freeze(EPOCH_TIME);

        PlanetaryPosition actual = PlanetaryPositionCalculator.position(PLANET);

        assertThat(actual.getEuclideanDistance()).isEqualTo(EUCLIDEAN_DISTANCE_OF_EPOCH_TIME);
        assertThat(actual.getTrueAnomaly()).isEqualTo(TRUE_ANOMALY_OF_EPOCH_TIME);
    }

    @Test
    void 역기점_한세기_후_행성의_위치를_가져옵니다() {
        TimeFreezer.freeze(EPOCH_TIME.plusYears(100));

        PlanetaryPosition actual = PlanetaryPositionCalculator.position(PLANET);

        assertThat(actual.getEuclideanDistance()).isEqualTo(EUCLIDEAN_DISTANCE_OF_A_CENTURY_AFTER_EPOCH_TIME);
        assertThat(actual.getTrueAnomaly()).isEqualTo(TRUE_ANOMALY_OF_A_CENTURY_AFTER_EPOCH_TIME);
    }

    @Test
    void 역기점_한세기_전_행성의_위치를_가져옵니다() {
        TimeFreezer.freeze(EPOCH_TIME.minusYears(100));

        PlanetaryPosition actual = PlanetaryPositionCalculator.position(PLANET);

        assertThat(actual.getEuclideanDistance()).isEqualTo(EUCLIDEAN_DISTANCE_OF_A_CENTURY_BEFORE_EPOCH_TIME);
        assertThat(actual.getTrueAnomaly()).isEqualTo(TRUE_ANOMALY_OF_A_CENTURY_BEFORE_EPOCH_TIME);
    }

    @Test
    void 행성의_위치를_가져올_때_유효하지_않은_값이면_안된다() {
        assertThatThrownBy(() -> PlanetaryPositionCalculator.position(null)).isInstanceOf(IllegalArgumentException.class);
    }
}
