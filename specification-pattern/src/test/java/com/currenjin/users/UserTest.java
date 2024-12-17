package com.currenjin.users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    public static final String NAME = "currenjin";
    public static final int AGE = 24;

    @Test
    void field_test() {
        User user = new User(NAME, AGE);

        assertEquals(NAME, user.getName());
        assertEquals(AGE, user.getAge());
    }

    @Test
    void age_cannot_be_less_than_zero() {
        assertThrows(IllegalArgumentException.class, () -> new User(NAME, -1));
    }

    @Test
    void name_cannot_be_null_or_blank() {
        assertThrows(IllegalArgumentException.class, () -> new User("", AGE));
        assertThrows(IllegalArgumentException.class, () -> new User(" ", AGE));
        assertThrows(IllegalArgumentException.class, () -> new User(null, AGE));
    }
}