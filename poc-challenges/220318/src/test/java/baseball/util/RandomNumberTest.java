package baseball.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RandomNumberTest {
    @Test
    void random_size() {
        List<Integer> numbers = RandomNumber.generate(3, 1, 9);

        assertThat(numbers.size()).isEqualTo(3);
    }

    @Test
    void random_range() {
        List<Integer> numbers = RandomNumber.generate(3, 1, 9);

        numbers.forEach(n -> {
            assertThat(n).isGreaterThan(0);
            assertThat(n).isLessThan(10);
        });
    }
}