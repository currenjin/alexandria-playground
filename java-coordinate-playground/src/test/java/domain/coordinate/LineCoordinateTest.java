package domain.coordinate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class LineCoordinateTest {
	private final int MAX_SIZE = 2;
	private final double X = 0.0;
	private final double Y = 1.1;
	private final Coordinate coordinate = Coordinates.of(X, Y);

	@Test
	void creation() {
		LineCoordinate lineCoordinate = new LineCoordinate();

		assertThat(lineCoordinate).isInstanceOf(LineCoordinate.class);
	}

	@Test
	void creation_without_max_size() {
		LineCoordinate lineCoordinate = new LineCoordinate();

		assertThat(lineCoordinate).isInstanceOf(LineCoordinate.class);
	}

	@Test
	void creationMaxSize() {
		LineCoordinate lineCoordinate = new LineCoordinate();

		assertThat(lineCoordinate.getMaxSize()).isEqualTo(MAX_SIZE);
	}

	@Test
	void creationSize() {
		LineCoordinate lineCoordinate = new LineCoordinate();

		assertThat(lineCoordinate.getCoordinateSize()).isEqualTo(0);
	}

	@Test
	void addCoordinate() {
		LineCoordinate lineCoordinate = new LineCoordinate();
		assertThat(lineCoordinate.getCoordinateSize()).isEqualTo(0);

		lineCoordinate.addCoordinate(coordinate);
		assertThat(lineCoordinate.getCoordinateSize()).isEqualTo(1);

		lineCoordinate.addCoordinate(coordinate);
		assertThat(lineCoordinate.getCoordinateSize()).isEqualTo(2);
	}

	@Test
	void throwExceptionWhenMaximumSizeExceeded() {
		LineCoordinate lineCoordinate = new LineCoordinate();

		for (int i = 0; i < MAX_SIZE; i++) { lineCoordinate.addCoordinate(coordinate); }

		assertThatThrownBy(() -> lineCoordinate.addCoordinate(coordinate))
			.hasMessageContaining("Maximum coordinate size exceeded");
	}

	@Test
	void printCoordinates() {
		LineCoordinate lineCoordinate = new LineCoordinate();
		lineCoordinate.addCoordinate(coordinate);
		lineCoordinate.addCoordinate(coordinate);

		String actual = lineCoordinate.printCoordinates();

		assertThat(actual).isEqualTo(
				String.format("%s-%s", coordinate.print(), coordinate.print())
		);
	}
}