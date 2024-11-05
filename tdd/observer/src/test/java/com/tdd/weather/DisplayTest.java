package com.tdd.weather;

import org.junit.jupiter.api.Test;

class DisplayTest {
    @Test
    void name() {
        Display display = new Display();

        display.update(1F, 2F, 3F);
    }
}