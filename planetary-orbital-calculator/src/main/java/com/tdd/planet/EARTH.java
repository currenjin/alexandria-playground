package com.tdd.planet;

import com.tdd.domain.Orbit;

public class EARTH {
    public static final Double AU = 149597870.0;

    public static final Double LONG_RADIUS = 1.00000261 * AU;
    public static final Double ECCENTRICITY = 0.01671123;
    public static final Double INCLINATION = -0.00001531;
    public static final Double LONGITUDE_OF_ASCENDING_NODE = 0.0;
    public static final Double AVERAGE_LONGITUDE = 100.46457166;
    public static final Double PERIHELION_LONGITUDE = 102.93768193;

    public static final Orbit CHANGE_PER_CENTURY = Orbit.of(
            0.00000562 * AU,
            -0.00004392,
            -0.01294668,
            0.0,
            35999.37244981,
            0.32327364);
}
