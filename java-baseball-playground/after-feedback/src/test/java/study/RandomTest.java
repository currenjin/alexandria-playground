package study;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomTest {
    Random random = new Random();

    @Test
    void random_size() {
        IntStream ints = random.ints(3);
        assertThat(ints.count()).isEqualTo(3);
    }

    @Test
    void random_range() {
        IntStream ints = random.ints(100, 1, 10);
        ints.forEach(i -> {
            assertThat(i).isGreaterThan(0);
            assertThat(i).isLessThan(10);
        });
    }
}
