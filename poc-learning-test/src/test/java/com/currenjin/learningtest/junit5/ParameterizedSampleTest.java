package com.currenjin.learningtest.junit5;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParameterizedSampleTest {

    @ParameterizedTest
    @ValueSource(strings = {"racecar", "radar"})
    void palindromes(String candidate) {
        assertTrue(isPalindrome(candidate));
    }

    @ParameterizedTest
    @EnumSource
    void enums(DEFCON defcon) {
        System.out.println(defcon);
    }

    private boolean isPalindrome(String character) {
        return character.contentEquals(new StringBuilder(character).reverse());
    }
}
