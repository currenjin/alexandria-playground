package domain.coordinate;

public class Coordinate {
	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	private final double x;
	private final double y;

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public String print() {
		return String.format("(%s,%s)", this.x, this.y);
	}
}
