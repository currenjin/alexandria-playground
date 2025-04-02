package com.tdd.application;

import com.tdd.util.TimeFreezer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EccentricityAnomalyCalculatorTest {

    public static final Double EPOCH_ECCENTRICITY = 0.01671123;
    public static final Double EPOCH_AVERAGE_LONGITUDE = 100.46457166;
    public static final Double EPOCH_PERIHELION_LONGITUDE = 102.93768193;

    @AfterEach
    void tearDown() {
        TimeFreezer.rollback();
    }

    @Test
    void 편심_이각을_계산한다() {
        double actual = EccentricityAnomalyCalculator.calculate(EPOCH_ECCENTRICITY, EPOCH_AVERAGE_LONGITUDE, EPOCH_PERIHELION_LONGITUDE);

        assertThat(actual).isEqualTo(-0.044630145101967715);
    }

    @Test
    void 편심_이각을_계산시_이심률이_유효하지_않으면_안된다() {
        assertThatThrownBy(() ->
                EccentricityAnomalyCalculator.calculate(null, EPOCH_AVERAGE_LONGITUDE, EPOCH_PERIHELION_LONGITUDE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 편심_이각을_계산시_평균_적경이_유효하지_않으면_안된다() {
        assertThatThrownBy(() ->
                EccentricityAnomalyCalculator.calculate(EPOCH_ECCENTRICITY, null, EPOCH_PERIHELION_LONGITUDE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 편심_이각을_계산시_근일점_적경이_유효하지_않으면_안된다() {
        assertThatThrownBy(() ->
                EccentricityAnomalyCalculator.calculate(EPOCH_ECCENTRICITY, EPOCH_AVERAGE_LONGITUDE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 평균근점이각은_평균_경도에서_근일점_경도를_뺀_값이다() {
        double actual = EccentricityAnomalyCalculator.calculateMeanAnomaly(EPOCH_AVERAGE_LONGITUDE, EPOCH_PERIHELION_LONGITUDE);

        assertThat(actual).isEqualTo(EPOCH_AVERAGE_LONGITUDE - EPOCH_PERIHELION_LONGITUDE);
    }

    @Test
    void 평균근점이각_계산시_평균_경도가_유효하지_않으면_안된다() {
        assertThatThrownBy(() ->
                EccentricityAnomalyCalculator.calculateMeanAnomaly(null, EPOCH_PERIHELION_LONGITUDE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 평균근점이각_계산시_근일점_경도가_유효하지_않으면_안된다() {
        assertThatThrownBy(() ->
                EccentricityAnomalyCalculator.calculateMeanAnomaly(EPOCH_AVERAGE_LONGITUDE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}