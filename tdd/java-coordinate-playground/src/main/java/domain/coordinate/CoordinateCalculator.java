package domain.coordinate;

public class CoordinateCalculator {
    public static double calculateLineDistance(Coordinate firstCoordinate, Coordinate secondCoordinate) {
        double firstTerm = getPow(firstCoordinate.getX() - secondCoordinate.getX());
        double secondTerm = getPow(firstCoordinate.getY() - secondCoordinate.getY());

        return Math.sqrt(firstTerm + secondTerm);
    }

    private static double getPow(double value) {
        return Math.pow(value, 2);
    }
}
