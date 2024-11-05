package baseball;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidateTest {

    @Test
    void 숫자_1_9_사이인지_검증() {
        assertThat(Validator.validNo(1)).isTrue();
        assertThat(Validator.validNo(9)).isTrue();
        assertThat(Validator.validNo(0)).isFalse();
        assertThat(Validator.validNo(10)).isFalse();
    }

    @Test
    void 세자리_숫자인지_검증() {
        assertThat(Validator.validSize(3, Lists.newArrayList(1, 2, 3))).isTrue();
        assertThat(Validator.validSize(3, Lists.newArrayList(1, 2))).isFalse();
        assertThat(Validator.validSize(3, Lists.newArrayList(1))).isFalse();
        assertThat(Validator.validSize(3, Lists.newArrayList(1, 2, 3, 4))).isFalse();
    }

    @Test
    void 중복_검증() {
        assertThat(Validator.validNotDuplicate(Lists.newArrayList(1, 2, 3, 4, 5))).isTrue();
        assertThat(Validator.validNotDuplicate(Lists.newArrayList(1, 2, 2))).isFalse();
        assertThat(Validator.validNotDuplicate(Lists.newArrayList(1))).isTrue();
    }
}
