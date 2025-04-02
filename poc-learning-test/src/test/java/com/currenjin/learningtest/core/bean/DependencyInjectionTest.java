package com.currenjin.learningtest.core.bean;

import com.currenjin.learningtest.core.bean.ExampleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DependencyInjectionTest {

    @Autowired
    private ExampleService exampleService;

    @Test
    void dependency_injection() {
        assertNotNull(exampleService);
    }

    @Test
    void call_method() {
        assertEquals("hi", exampleService.getMessage());
    }
}
