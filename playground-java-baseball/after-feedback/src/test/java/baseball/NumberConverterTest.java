package baseball;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberConverterTest {
    @Test
    void convert_size() {
        List<Integer> numbers = NumberConverter.convertBy("123");
        assertThat(numbers.size()).isEqualTo(3);
    }

    @Test
    void check_convert_value() {
        List<Integer> numbers = NumberConverter.convertBy("123");
        assertThat(numbers.get(0)).isEqualTo(1);
        assertThat(numbers.get(1)).isEqualTo(2);
        assertThat(numbers.get(2)).isEqualTo(3);
    }
}
