package study;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ConvertStringToNumbersTest {
    // 문자열에 담긴 수를 수의 배열로 변환한다. v

    @Test
    void convert() {
        NumbersConverter converter = new NumbersConverter();

        List<Integer> actual = converter.convert("123");
        List<Integer> actual2 = converter.convert("1235");
        List<Integer> actual3 = converter.convert("1237");

        assertThat(actual).isEqualTo(Lists.newArrayList(1, 2, 3));
        assertThat(actual2).isEqualTo(Lists.newArrayList(1, 2, 3, 5));
        assertThat(actual3).isEqualTo(Lists.newArrayList(1, 2, 3, 7));
    }
}
