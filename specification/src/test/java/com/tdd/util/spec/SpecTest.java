package com.tdd.util.spec;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecTest {

    @Test
    void first_spec() {
        // name 이 정현진이 아니고
        // name 의 길이가 5 이상이고 나이가 18 이하이거나 20 이상이며
        // 키가 180 이상인 경우
        // 또는 위 조건 모두 아닌 경우 키 180 이상이면 통과

        // (name != 정현진 && name.length >= 5 && (age <= 18 || age >=20))
        // || (height >= 180)

        Map<String, Object> factor = Maps.newHashMap("name", "foo");
        factor.put("age", 213);
        factor.put("height", 4);
    }

    @Test
    void age_or_lessthan_greatthan() {
        // age <= 18 || age >= 20
        Map<String, Object> factor = Maps.newHashMap("age", 213);

        Condition condition = new OrCondition(
                new LessThanEqualCondition("age", 18),
                new GreaterThanEqual("age", 20)
        );

        assertThat(condition.isSatisfy(factor)).isTrue();
    }

    @Test
    void age_lessThanEqual() {
        Map<String, Object> factor1 = Maps.newHashMap("age", 19);
        Map<String, Object> factor2 = Maps.newHashMap("age", 18);
        Map<String, Object> factor3 = Maps.newHashMap("age", 17);

        Condition condition = new LessThanEqualCondition("age", 18);

        assertThat(condition.isSatisfy(factor1)).isFalse();
        assertThat(condition.isSatisfy(factor2)).isTrue();
        assertThat(condition.isSatisfy(factor3)).isTrue();
    }

    @Test
    void age_greatThanEqual() {
        Map<String, Object> factor1 = Maps.newHashMap("age", 19);
        Map<String, Object> factor2 = Maps.newHashMap("age", 18);
        Map<String, Object> factor3 = Maps.newHashMap("age", 17);

        Condition condition = new GreaterThanEqual("age", 18);

        assertThat(condition.isSatisfy(factor1)).isTrue();
        assertThat(condition.isSatisfy(factor2)).isTrue();
        assertThat(condition.isSatisfy(factor3)).isFalse();
    }
}
