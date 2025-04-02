package com.tdd.specification;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MapSpecificationTest {
    @Test
    void satisfy() {
        Map<String, String> factor1 = Maps.newHashMap("name", "정현진");
        Map<String, String> factor2 = Maps.newHashMap("name", "정아무개");

        Specification spec = new MapSpecification("name", "정현진");

        assertThat(spec.isSatisfy(factor1)).isTrue();
        assertThat(spec.isSatisfy(factor2)).isFalse();
    }
}
