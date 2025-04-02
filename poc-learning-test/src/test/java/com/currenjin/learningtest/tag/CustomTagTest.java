package com.currenjin.learningtest.tag;

import com.currenjin.learningtest.tag.annotation.CleanTest;
import com.currenjin.learningtest.tag.annotation.DirtyTest;

public class CustomTagTest {

    @CleanTest
    void clean_first() {
    }

    @CleanTest
    void clean_second() {
    }

    @DirtyTest
    void dirty_first() {
    }

    @DirtyTest
    void dirty_second() {
    }
}
