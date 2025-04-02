package com.tdd.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrbitTest {

    private static final Double LONG_RADIUS = 1.111;
    private static final Double ECCENTRICITY = 1.11;
    private static final Double INCLINATION = 1.1;
    private static final Double LONGITUDE_OF_ASCENDING_NODE = 1.1;
    private static final Double AVERAGE_LONGITUDE = 1.1;
    private static final Double PERIHELION_LONGITUDE = 1.1;

    @Test
    void 궤도를_생성합니다() {
        Orbit actual = Orbit.of(LONG_RADIUS,
                ECCENTRICITY,
                INCLINATION,
                LONGITUDE_OF_ASCENDING_NODE,
                AVERAGE_LONGITUDE,
                PERIHELION_LONGITUDE);

        assertThat(actual).isInstanceOf(Orbit.class);
    }

    @Test
    void 궤도_데이터가_삽입한_데이터와_일치합니다() {
        Orbit actual = Orbit.of(LONG_RADIUS,
                ECCENTRICITY,
                INCLINATION,
                LONGITUDE_OF_ASCENDING_NODE,
                AVERAGE_LONGITUDE,
                PERIHELION_LONGITUDE);

        assertThat(actual.getLongRadius()).isEqualTo(LONG_RADIUS);
        assertThat(actual.getEccentricity()).isEqualTo(ECCENTRICITY);
        assertThat(actual.getInclination()).isEqualTo(INCLINATION);
        assertThat(actual.getLongitudeOfAscendingNode()).isEqualTo(LONGITUDE_OF_ASCENDING_NODE);
        assertThat(actual.getAverageLongitude()).isEqualTo(AVERAGE_LONGITUDE);
        assertThat(actual.getPerihelionLongitude()).isEqualTo(PERIHELION_LONGITUDE);
    }
}