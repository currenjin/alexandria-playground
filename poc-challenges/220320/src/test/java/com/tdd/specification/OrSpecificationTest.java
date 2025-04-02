package com.tdd.specification;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OrSpecificationTest {
    @Test
    void or_satisfy() {
        // name = 정현진
        // age > 18
        Map<String, Object> factor = Maps.newHashMap("age", 20);

        Specification spec = new OrSpecification(
                new MapSpecification("name", "정현진"),
                new GreaterThanSpecification("age", 18));

        assertThat(spec.isSatisfy(factor)).isTrue();

        factor.put("name", "정현진");
        assertThat(spec.isSatisfy(factor)).isTrue();
    }
}
