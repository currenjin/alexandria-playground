package domain.coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineCoordinate {
	public static final int LINE_COORDINATE_SIZE = 2;
	private final List<Coordinate> coordinates = new ArrayList<>();
	private final int maxSize;

	public LineCoordinate() {
		this.maxSize = LINE_COORDINATE_SIZE;
	}

	public void addCoordinate(Coordinate coordinate) {
		if (getCoordinateSize() >= this.maxSize) {
			throw new IllegalStateException("Maximum coordinate size exceeded. Max coordinate size is " + this.maxSize);
		}

		this.coordinates.add(coordinate);
	}

	public int getCoordinateSize() {
		return this.coordinates.size();
	}

	public int getMaxSize() {
		return this.maxSize;
	}

	public String printCoordinates() {
		return this.coordinates
				.stream()
				.map(Coordinate::print)
				.collect(Collectors.joining("-"));
	}
}
