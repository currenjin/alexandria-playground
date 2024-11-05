package com.tdd.util.spec;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AndConditionTest {

    @Test
    @DisplayName("is satisfied for multiple condition")
    void multiple_condition() {
        Map<String, Object> factor = Maps.newHashMap("name", "foo");
        AndCondition sut = new AndCondition(new AndCondition(
                MapCondition.expected("name", "foo"),
                MapCondition.expected("age", "18")),
                MapCondition.expected("height", "180"));

        assertThat(sut.isSatisfy(factor)).isFalse();

        factor.put("age", "18");
        factor.put("height", "180");
        assertThat(sut.isSatisfy(factor)).isTrue();
    }
}