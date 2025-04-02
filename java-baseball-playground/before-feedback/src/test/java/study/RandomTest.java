package study;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomTest {

    @Test
    void random_ints_type() {
        Random random = new Random();

        IntStream actual = random.ints(1);

        actual.forEach(value -> assertThat(value).isExactlyInstanceOf(Integer.class));
    }

    @Test
    void random_ints_size() {
        Random random = new Random();

        IntStream actual = random.ints(3);

        assertThat(actual.count()).isEqualTo(3);
    }

    @Test
    void random_ints_of_range() {
        Random random = new Random();

        IntStream actual = random.ints(100, 1, 9);

        actual.forEach(value -> {
            assertThat(value).isExactlyInstanceOf(Integer.class);
            assertThat(value).isLessThan(10);
            assertThat(value).isGreaterThan(0);
        });
    }
}
