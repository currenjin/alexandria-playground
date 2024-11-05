package com.tdd.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlanetaryPositionTest {

    public static final double EUCLIDEAN_DISTANCE = 1.1;
    public static final double TRUE_ANOMALY = 1.1;

    @Test
    void 행성의_위치를_생성합니다() {
        PlanetaryPosition actual = PlanetaryPosition.of(EUCLIDEAN_DISTANCE, TRUE_ANOMALY);

        assertThat(actual.getEuclideanDistance()).isEqualTo(EUCLIDEAN_DISTANCE);
        assertThat(actual.getTrueAnomaly()).isEqualTo(TRUE_ANOMALY);
    }
}