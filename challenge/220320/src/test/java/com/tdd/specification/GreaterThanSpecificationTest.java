package com.tdd.specification;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GreaterThanSpecificationTest {
    @Test
    void greater_than_satisfy() {
        // age > 18
        Map<String, Object> factor1 = Maps.newHashMap("age", 17);
        Map<String, Object> factor2 = Maps.newHashMap("age", 18);
        Map<String, Object> factor3 = Maps.newHashMap("age", 19);
        Map<String, Object> factor4 = Maps.newHashMap("age", 20);

        Specification specification = new GreaterThanSpecification("age", 18);

        assertThat(specification.isSatisfy(factor1)).isFalse();
        assertThat(specification.isSatisfy(factor2)).isFalse();
        assertThat(specification.isSatisfy(factor3)).isTrue();
        assertThat(specification.isSatisfy(factor4)).isTrue();
    }
}
