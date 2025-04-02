package com.tdd.util;

import java.time.LocalDateTime;

public final class TimeFreezer {

    public static void freeze(LocalDateTime localDateTime) {
        Clocks.freeze(localDateTime);
    }

    public static void rollback() {
        Clocks.resetToSystem();
    }
}
