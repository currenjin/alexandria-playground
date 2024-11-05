package com.currenjin.learningtest.tag;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    @Tag("clean")
    void clean_first() {
    }

    @Test
    @Tag("clean")
    void clean_second() {
    }

    @Test
    @Tag("dirty")
    void dirty_first() {
    }

    @Test
    @Tag("dirty")
    void dirty_second() {
    }
}