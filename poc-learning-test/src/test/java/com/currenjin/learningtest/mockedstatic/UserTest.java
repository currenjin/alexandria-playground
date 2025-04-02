package com.currenjin.learningtest.mockedstatic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    public static final String NAME = "currenjin";

    @Test
    void 이름을_가진_유저를_생성합니다() {
        User actual = new User(NAME);

        assertThat(actual.getName()).isEqualTo(NAME);
    }

    @Test
    void super_유저를_생성합니다() {
        User actual = User.superUserOf();

        assertThat(actual.getName()).isEqualTo("super");
    }
}