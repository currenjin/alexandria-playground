package com.tdd.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EclipticCoordinateTest {

    private static final double X = 1.1;
    private static final double Y = 2.1;

    @Test
    void 황도_좌표를_생성합니다() {
        EclipticCoordinate actual = EclipticCoordinate.of(X, Y);

        assertThat(actual.getX()).isEqualTo(X);
        assertThat(actual.getY()).isEqualTo(Y);
    }
}