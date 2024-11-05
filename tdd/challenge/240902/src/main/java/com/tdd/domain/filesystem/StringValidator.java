package com.tdd.domain.filesystem;

public class StringValidator {
    public static void validate(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        if (string.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
    }
}
