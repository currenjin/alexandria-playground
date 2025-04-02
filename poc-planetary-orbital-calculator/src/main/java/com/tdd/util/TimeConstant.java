package com.tdd.util;

import java.time.LocalDateTime;

public class TimeConstant {
    static final LocalDateTime J2000 = LocalDateTime.of(2000, 1, 1, 12, 0, 0);
    static final long DAY = 60 * 60 * 24;
    static final double YEAR = 365.25;
    static final double CENTURY = 100 * YEAR;
}
