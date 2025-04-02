package baseball.util;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidatorTest {

    Validator validator = new Validator();

    @Test
    void validNumber() {
        assertThat(validator.validNumber(1)).isTrue();
        assertThat(validator.validNumber(9)).isTrue();
        assertThat(validator.validNumber(0)).isFalse();
        assertThat(validator.validNumber(10)).isFalse();
    }

    @Test
    void validDuplicate() {
        assertThat(validator.isNotDuplicateNumber(Lists.newArrayList(1, 2, 3))).isTrue();
        assertThat(validator.isNotDuplicateNumber(Lists.newArrayList(1, 2, 4))).isTrue();
        assertThat(validator.isNotDuplicateNumber(Lists.newArrayList(1, 2, 2))).isFalse();
    }

    @Test
    void validRange() {
        assertThat(validator.validSize(2, Lists.newArrayList(1, 2))).isTrue();
        assertThat(validator.validSize(3, Lists.newArrayList(1, 2, 3))).isTrue();
        assertThat(validator.validSize(1, Lists.newArrayList(1, 2))).isFalse();
        assertThat(validator.validSize(4, Lists.newArrayList(1, 2, 2))).isFalse();
    }
}
