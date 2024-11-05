package domain.coordinate;

public class Coordinates {
	public static Coordinate of(double x, double y) {
		CoordinateValidator.validate(x, y);
		return new Coordinate(x, y);
	}
}
