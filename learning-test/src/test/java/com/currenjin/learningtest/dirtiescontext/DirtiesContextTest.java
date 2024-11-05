package com.currenjin.learningtest.dirtiescontext;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class DirtiesContextTest {

    @Autowired
    private UserCache sut;

    @Mock
    User user_1;

    @Test
    @Order(1)
    void user_1_을_추가합니다() {
        sut.addUser(user_1);

        assertThat(sut.getUserList().size()).isEqualTo(1);
    }

    @Test
    @Order(2)
    void user_list_를_확인합니다() {
        assertThat(sut.getUserList().size()).isEqualTo(1);
    }

    @Test
    @Order(3)
    void user_1_을_다시_한_번_추가합니다() {
        sut.addUser(user_1);

        assertThat(sut.getUserList().size()).isEqualTo(2);
    }

    @Test
    @Order(4)
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void user_list_를_확인합니다_with_dirties_context() {
        assertThat(sut.getUserList().size()).isEqualTo(0);
    }
}
