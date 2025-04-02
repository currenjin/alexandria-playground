package com.tdd.util;

public class KeplerThirdLaw {
    public static final double G = 6.67430e-11; // 중력 상수 (m^3 kg^-1 s^-2)
    public static final double SOLAR_MASS = 1.98847e30; // 태양 질량 (kg)
    public static final double AU = 1.49597871e11; // 천문 단위 (m)
    private static final double EARTH_YEAR_SECONDS = 365.25 * 24 * 60 * 60; // 1 지구년 (초)

    // 케플러의 제 3법칙: T^2 = (4π^2 / GM) * a^3
    public static double calculateOrbitalPeriod(double semiMajorAxis, double centralMass) {
        validate(semiMajorAxis, centralMass);

        double a = semiMajorAxis * AU;
        double m = centralMass * SOLAR_MASS;

        double periodSquared = (4 * Math.PI * Math.PI / (G * m)) * Math.pow(a, 3);
        double periodSeconds = Math.sqrt(periodSquared);

        return periodSeconds / EARTH_YEAR_SECONDS; // 지구년 단위로 변환
    }

    private static void validate(double semiMajorAxis, double centralMass) {
        if (semiMajorAxis <= 0.0) {
            throw new IllegalArgumentException("The semi-major axis must be greater than 0.");
        }

        if (centralMass <= 0.0) {
            throw new IllegalArgumentException("The central mass must be greater than 0.");
        }
    }
}
