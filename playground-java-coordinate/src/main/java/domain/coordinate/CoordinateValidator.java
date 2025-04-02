package domain.coordinate;

public class CoordinateValidator {
	private static final double MIN_VALUE = 0.0;
	private static final double MAX_VALUE = 24.0;

	static void validate(double x, double y) {
		if (x < MIN_VALUE || y < MIN_VALUE) {
			throw new IllegalArgumentException(
				"Minimum coordinate is smaller than the minimum value. Minimum value: " + MIN_VALUE);
		}

		if (x > MAX_VALUE || y > MAX_VALUE) {
			throw new IllegalArgumentException(
				"Maximum coordinate is bigger than the maximum value. Maximum value: " + MAX_VALUE);
		}
	}
}
