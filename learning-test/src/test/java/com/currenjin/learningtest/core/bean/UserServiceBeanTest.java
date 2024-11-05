package com.currenjin.learningtest.core.bean;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
class UserServiceBeanTest {
    @Test
    void lifeCycleTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(UserService.class);
        context.register(UserRepository.class);
        context.refresh();

        context.getBean(UserService.class);

        System.out.println("============================");

        context.close();
    }
}