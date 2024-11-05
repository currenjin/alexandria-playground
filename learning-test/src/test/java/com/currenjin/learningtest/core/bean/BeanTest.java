package com.currenjin.learningtest.core.bean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootTest
class BeanTest {
    @Autowired
    ApplicationContext applicationContext;

    @Test
    void beanTest() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();

        Arrays.stream(beanNames).forEach(System.out::println);
    }
}
