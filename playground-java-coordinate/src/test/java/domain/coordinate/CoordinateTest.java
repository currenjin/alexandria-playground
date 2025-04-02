package domain.coordinate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CoordinateTest {

	public static final double X = 0.0;
	public static final double Y = 1.0;
	private static final double X_24 = 24.0;
	private static final double Y_24 = 24.0;
	private static final double X_0 = 0.0;
	private static final double Y_0 = 0.0;

	@Test
	void creation() {
		Coordinate coordinate = Coordinates.of(X, Y);

		assertThat(coordinate).isInstanceOf(Coordinate.class);
	}

	@Test
	void min0_max24() {
		Coordinate minCoordinate = Coordinates.of(X_0, Y_0);
		Coordinate maxCoordinate = Coordinates.of(X_24, Y_24);

		assertThat(minCoordinate).isInstanceOf(Coordinate.class);
		assertThat(maxCoordinate).isInstanceOf(Coordinate.class);
	}

	@Test
	void throwExceptionWhenMaximumExceeded() {
		String exceptionMessage = "Maximum coordinate is bigger than the maximum value.";

		assertThatThrownBy(() -> Coordinates.of(X_24 + 0.1, Y_24)).hasMessageContaining(exceptionMessage);
		assertThatThrownBy(() -> Coordinates.of(X_24, Y_24 + 0.1)).hasMessageContaining(exceptionMessage);
	}

	@Test
	void throwExceptionWhenMinimumExceeded() {
		String exceptionMessage = "Minimum coordinate is smaller than the minimum value.";

		assertThatThrownBy(() -> Coordinates.of(X_0 - 0.1, Y_0)).hasMessageContaining(exceptionMessage);
		assertThatThrownBy(() -> Coordinates.of(X_0, Y_0 - 0.1)).hasMessageContaining(exceptionMessage);
	}

	@Test
	void get() {
		Coordinate coordinate = Coordinates.of(X, Y);

		assertThat(coordinate.getX()).isEqualTo(X);
		assertThat(coordinate.getY()).isEqualTo(Y);
	}
}