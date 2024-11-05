package com.tdd.application;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArgumentOfPeriapsisCalculatorTest {

    public static final Double LONGITUDE_OF_ASCENDING_NODE = 0.0;
    public static final Double PERIHELION_LONGITUDE = 102.93768193;

    @Test
    void 근일점_편각은_근일점_경도에서_승교적_적경을_뺀_값이다() {
        double actual = ArgumentOfPeriapsisCalculator.calculate(PERIHELION_LONGITUDE, LONGITUDE_OF_ASCENDING_NODE);

        assertThat(actual).isEqualTo(PERIHELION_LONGITUDE - LONGITUDE_OF_ASCENDING_NODE);
    }

    @Test
    void 근일점_경도가_유효하지_않은_값이면_안된다() {
        assertThatThrownBy(() ->
                ArgumentOfPeriapsisCalculator.calculate(null, LONGITUDE_OF_ASCENDING_NODE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 승교점_적경이_유효하지_않은_값이면_안된다() {
        assertThatThrownBy(() ->
                ArgumentOfPeriapsisCalculator.calculate(PERIHELION_LONGITUDE, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}