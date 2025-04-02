package com.currenjin.learningtest.mockedstatic;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class MockedStaticTest {

    public static final String CURRENJIN = "currenjin";

    private MockedStatic user;

    @BeforeEach
    void beforeEach() {
        user = mockStatic(User.class);
    }

    @AfterEach
    void afterEach() {
        user.close();
    }

    @Test
    void super_유저를_생성합니다() {
        given(User.superUserOf()).willReturn(new User(CURRENJIN));

        User actual = User.superUserOf();

        assertThat(actual.getName()).isEqualTo(CURRENJIN);
    }
}
