package com.currenjin.learningtest.core.bean;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
class FooBeanTest {

    @Test
    void lifeCycleTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(FooBean.class);
        context.refresh();

        context.getBean(FooBean.class);

        context.close();
    }
}