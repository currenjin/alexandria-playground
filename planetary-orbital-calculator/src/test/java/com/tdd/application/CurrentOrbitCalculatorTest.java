package com.tdd.application;

import com.tdd.domain.Orbit;
import com.tdd.domain.PlanetOrbit;
import com.tdd.planet.EARTH;
import com.tdd.util.TimeFreezer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CurrentOrbitCalculatorTest {

    public static final LocalDateTime A_CENTURY_AFTER_EPOCH_TIME = LocalDateTime.of(2100, 1, 1, 12, 0, 0);
    private static final LocalDateTime TWO_CENTURY_AFTER_EPOCH_TIME = LocalDateTime.of(2200, 1, 2, 12, 0, 0);
    private static final LocalDateTime A_CENTURY_BEFORE_EPOCH_TIME = LocalDateTime.of(1899, 12, 31, 12, 0, 0);
    private static final LocalDateTime EPOCH_TIME = LocalDateTime.of(2000, 1, 1, 12, 0, 0);

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
    void 역기점으로부터_한_세기가_지난_시점의_장반경_값() {
        TimeFreezer.freeze(A_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getLongRadius())
                .isEqualTo(PLANET.getLongRadius() + PLANET.getChangePerCentury().getLongRadius());
    }

    @Test
    void 역기점으로부터_두_세기가_지난_시점의_장반경_값() {
        TimeFreezer.freeze(TWO_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getLongRadius())
                .isEqualTo(PLANET.getLongRadius() + (PLANET.getChangePerCentury().getLongRadius() * 2));
    }

    @Test
    void 역기점으로부터_한_세기_이전_시점의_장반경_값() {
        TimeFreezer.freeze(A_CENTURY_BEFORE_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getLongRadius())
                .isEqualTo(PLANET.getLongRadius() + (PLANET.getChangePerCentury().getLongRadius() * -1));
    }

    @Test
    void 역기점의_장반경_값() {
        TimeFreezer.freeze(EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getLongRadius())
                .isEqualTo(PLANET.getLongRadius());
    }

    @Test
    void 역기점으로부터_한_세기가_지난_시점의_이심률_값() {
        TimeFreezer.freeze(A_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getEccentricity())
                .isEqualTo(PLANET.getEccentricity() + PLANET.getChangePerCentury().getEccentricity());
    }

    @Test
    void 역기점으로부터_두_세기가_지난_시점의_이심률_값() {
        TimeFreezer.freeze(TWO_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getEccentricity())
                .isEqualTo(PLANET.getEccentricity() + (PLANET.getChangePerCentury().getEccentricity() * 2));
    }

    @Test
    void 역기점으로부터_한_세기_이전_시점의_이심률_값() {
        TimeFreezer.freeze(A_CENTURY_BEFORE_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getEccentricity())
                .isEqualTo(PLANET.getEccentricity() + (PLANET.getChangePerCentury().getEccentricity() * -1));
    }

    @Test
    void 역기점의_이심률_값() {
        TimeFreezer.freeze(EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getEccentricity())
                .isEqualTo(PLANET.getEccentricity());
    }

    @Test
    void 역기점으로부터_한_세기가_지난_시점의_기울기_값() {
        TimeFreezer.freeze(A_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getInclination())
                .isEqualTo(PLANET.getInclination() + PLANET.getChangePerCentury().getInclination());
    }

    @Test
    void 역기점으로부터_두_세기가_지난_시점의_기울기_값() {
        TimeFreezer.freeze(TWO_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getInclination())
                .isEqualTo(PLANET.getInclination() + (PLANET.getChangePerCentury().getInclination() * 2));
    }

    @Test
    void 역기점으로부터_한_세기_이전_시점의_기울기_값() {
        TimeFreezer.freeze(A_CENTURY_BEFORE_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getInclination())
                .isEqualTo(PLANET.getInclination() + (PLANET.getChangePerCentury().getInclination() * -1));
    }

    @Test
    void 역기점의_기울기_값() {
        TimeFreezer.freeze(EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getInclination())
                .isEqualTo(PLANET.getInclination());
    }

    @Test
    void 역기점으로부터_한_세기가_지난_시점의_승교점_적경_값() {
        TimeFreezer.freeze(A_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getLongitudeOfAscendingNode())
                .isEqualTo(PLANET.getLongitudeOfAscendingNode() + PLANET.getChangePerCentury().getLongitudeOfAscendingNode());
    }

    @Test
    void 역기점으로부터_두_세기가_지난_시점의_승교점_적경_값() {
        TimeFreezer.freeze(TWO_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getLongitudeOfAscendingNode())
                .isEqualTo(PLANET.getLongitudeOfAscendingNode() + (PLANET.getChangePerCentury().getLongitudeOfAscendingNode() * 2));
    }

    @Test
    void 역기점으로부터_한_세기_이전_시점의_승교점_적경_값() {
        TimeFreezer.freeze(A_CENTURY_BEFORE_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getLongitudeOfAscendingNode())
                .isEqualTo(PLANET.getLongitudeOfAscendingNode() + (PLANET.getChangePerCentury().getLongitudeOfAscendingNode() * -1));
    }

    @Test
    void 역기점의_승교점_적경_값() {
        TimeFreezer.freeze(EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getLongitudeOfAscendingNode())
                .isEqualTo(PLANET.getLongitudeOfAscendingNode());
    }

    @Test
    void 역기점으로부터_한_세기가_지난_시점의_평균_적경_값() {
        TimeFreezer.freeze(A_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getAverageLongitude())
                .isEqualTo(PLANET.getAverageLongitude() + PLANET.getChangePerCentury().getAverageLongitude());
    }

    @Test
    void 역기점으로부터_두_세기가_지난_시점의_평균_적경_값() {
        TimeFreezer.freeze(TWO_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getAverageLongitude())
                .isEqualTo(PLANET.getAverageLongitude() + (PLANET.getChangePerCentury().getAverageLongitude() * 2));
    }

    @Test
    void 역기점으로부터_한_세기_이전_시점의_평균_적경_값() {
        TimeFreezer.freeze(A_CENTURY_BEFORE_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getAverageLongitude())
                .isEqualTo(PLANET.getAverageLongitude() + (PLANET.getChangePerCentury().getAverageLongitude() * -1));
    }

    @Test
    void 역기점의_평균_적경_값() {
        TimeFreezer.freeze(EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getAverageLongitude())
                .isEqualTo(PLANET.getAverageLongitude());
    }

    @Test
    void 역기점으로부터_한_세기가_지난_시점의_근일점_적경_값() {
        TimeFreezer.freeze(A_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getPerihelionLongitude())
                .isEqualTo(PLANET.getPerihelionLongitude() + PLANET.getChangePerCentury().getPerihelionLongitude());
    }

    @Test
    void 역기점으로부터_두_세기가_지난_시점의_근일점_적경_값() {
        TimeFreezer.freeze(TWO_CENTURY_AFTER_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getPerihelionLongitude())
                .isEqualTo(PLANET.getPerihelionLongitude() + (PLANET.getChangePerCentury().getPerihelionLongitude() * 2));
    }

    @Test
    void 역기점으로부터_한_세기_이전_시점의_근일점_적경_값() {
        TimeFreezer.freeze(A_CENTURY_BEFORE_EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getPerihelionLongitude())
                .isEqualTo(PLANET.getPerihelionLongitude() + (PLANET.getChangePerCentury().getPerihelionLongitude() * -1));
    }

    @Test
    void 역기점의_근일점_적경_값() {
        TimeFreezer.freeze(EPOCH_TIME);

        Orbit actual = CurrentOrbitCalculator.of(PLANET);

        assertThat(actual.getPerihelionLongitude())
                .isEqualTo(PLANET.getPerihelionLongitude());
    }

}