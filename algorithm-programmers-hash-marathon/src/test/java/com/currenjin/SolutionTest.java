package com.currenjin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {
    @Test
    void sample1() {
        assertEquals("leo", new Solution().solution(new String[]{"leo", "kiki", "eden"}, new String[]{"eden", "kiki"}));
    }

    @Test
    void duplicateName() {
        assertEquals("mislav", new Solution().solution(
                new String[]{"mislav", "stanko", "mislav", "ana"},
                new String[]{"stanko", "ana", "mislav"}
        ));
    }
}
