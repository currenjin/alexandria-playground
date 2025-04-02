package com.currenjin.learningtest.collection;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionTest {
    @Nested
    class SetTest {
        @Test
        void duplicate() {
            Set<Integer> set = new HashSet<>(Set.of(1, 2, 3));

            set.add(3);

            assertThat(set.size()).isEqualTo(3);
        }
    }
}
