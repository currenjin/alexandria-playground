package domain.coordinate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CoordinateCalculatorTest {
    @Test
    void calculate_line_distance_coordinate() {
        Coordinate zeroCoordinate = Coordinates.of(10.0, 10.0);
        Coordinate oneCoordinate = Coordinates.of(14.0, 15.0);

        double actual = CoordinateCalculator.calculateLineDistance(zeroCoordinate, oneCoordinate);

        assertThat(actual).isEqualTo(6.4031242374328485);
    }
}