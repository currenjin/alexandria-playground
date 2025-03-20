package com.currenjin.learningtest.junit5;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParameterizedSampleTest {

    @ParameterizedTest
    @ValueSource(strings = {"racecar", "radar"})
    void palindromes(String candidate) {
        assertTrue(isPalindrome(candidate));
    }

    private boolean isPalindrome(String character) {
        return character.contentEquals(new StringBuilder(character).reverse());
    }
}
