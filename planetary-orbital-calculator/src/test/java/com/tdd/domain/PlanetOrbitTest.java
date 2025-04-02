package com.tdd.domain;

import com.tdd.planet.EARTH;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlanetOrbitTest {

    PlanetOrbit earth = PlanetOrbit.of(EARTH.LONG_RADIUS,
            EARTH.ECCENTRICITY,
            EARTH.INCLINATION,
            EARTH.LONGITUDE_OF_ASCENDING_NODE,
            EARTH.AVERAGE_LONGITUDE,
            EARTH.PERIHELION_LONGITUDE,
            EARTH.AU,
            EARTH.CHANGE_PER_CENTURY);

    @Test
    void 지구_궤도를_생성합니다() {
        assertThat(earth).isInstanceOf(Orbit.class);
        assertThat(earth.getAu()).isEqualTo(EARTH.AU);
        assertThat(earth.getChangePerCentury()).isEqualTo(EARTH.CHANGE_PER_CENTURY);
    }
}