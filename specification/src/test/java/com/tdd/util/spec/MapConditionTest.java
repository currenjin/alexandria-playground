package com.tdd.util.spec;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MapConditionTest {

    @Test
    @DisplayName("is satisfied because contain key and value")
    void map_contain() {
        Map<String, String> map = Maps.newHashMap("jar", "foo");

        assertThat(MapCondition.expected("jar", "xxxxx").isSatisfy(map)).isFalse();
        assertThat(MapCondition.expected("jar", "foo").isSatisfy(map)).isTrue();
    }
}