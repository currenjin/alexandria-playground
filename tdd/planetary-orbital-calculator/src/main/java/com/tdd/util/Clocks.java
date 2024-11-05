package com.tdd.util;

import java.time.LocalDateTime;

public abstract class Clocks implements Clock {
    private static final Clock SYSTEM_CLOCK;
    static {
        SYSTEM_CLOCK = new SystemClock();
    }

    private static Clock INSTANCE = SYSTEM_CLOCK;

    static final class SystemClock implements Clock {
        @Override
        public LocalDateTime getNow() {
            return LocalDateTime.now();
        }
    }

    static final class FrozenClock implements Clock {
        private final LocalDateTime localDateTime;

        public FrozenClock(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }


        @Override
        public LocalDateTime getNow() {
            return localDateTime;
        }
    }

    static Clock getInstance() {
        return INSTANCE;
    }

    public static LocalDateTime now() {
        return getInstance().getNow();
    }

    synchronized static void freeze(LocalDateTime localDateTime) {
        INSTANCE = new FrozenClock(localDateTime);
    }

    synchronized static void resetToSystem() {
        INSTANCE = SYSTEM_CLOCK;
    }

}
