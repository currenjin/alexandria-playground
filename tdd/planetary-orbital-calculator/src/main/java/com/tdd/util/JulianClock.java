package com.tdd.util;

import static java.time.ZoneOffset.UTC;

public class JulianClock {

    public static long elapsedDate() {
        return getEpochTime() / TimeConstant.DAY;
    }

    public static double elapsedCentury() {
        return elapsedDate() / TimeConstant.CENTURY;
    }

    private static long getEpochTime() {
        return Clocks.now().toEpochSecond(UTC) - TimeConstant.J2000.toEpochSecond(UTC);
    }
}
