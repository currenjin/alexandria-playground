package com.currenjin.learningtest.dirtiescontext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    User sut;

    @BeforeEach
    void setUp() {
         sut = new User();
    }

    @Test
    void constructor() {
        assertThat(sut).isInstanceOf(User.class);
    }
}