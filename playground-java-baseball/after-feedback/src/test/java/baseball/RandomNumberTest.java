package baseball;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomNumberTest {
    @Test
    void size() {
        List<Integer> randomNumbers = RandomNumber.generate(3, 1, 9);
        assertThat(randomNumbers.size()).isEqualTo(3);
    }

    @Test
    void range() {
        List<Integer> randomNumbers = RandomNumber.generate(3, 1, 9);
        randomNumbers.forEach(number -> {
            assertThat(number).isGreaterThan(0);
            assertThat(number).isLessThan(10);
        });
    }
}
