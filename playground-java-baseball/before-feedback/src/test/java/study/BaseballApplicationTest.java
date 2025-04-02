package study;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static study.NumbersConverter.convert;

public class BaseballApplicationTest {

    /*
    숫자 야구 애플리케이션
    - 숫자 저장
    - 스트라이크 탐색
    - 볼 탐색

     뭔진 모르겠지만 숫자를 세 개 넣는 어플리케이션을 호출한다. nothing 이라는 값을 리턴한다. v
     어플리케이션이 임의의 숫자 세 개를 갖도록 한다. v
     임의의 숫자 세 개는 서로 같은 수가 나오지 않아야 한다.
     입력한 숫자 중 임의의 숫자에도 존재하면 개수만큼 n볼이라고 답한다. v
     그 중 위치까지 같으면 개수만큼 n스트라이크라고 답한다. v
     */

    private static final List<Integer> NUMBERS_1_4_5 = Lists.newArrayList(1, 4, 5);

    NumberGenerator numberGenerator;
    BaseballApplication sut;

    @BeforeEach
    void setUp() {
        numberGenerator = mock(NumberGenerator.class);
        sut = new BaseballApplication(numberGenerator);
    }

    @Test
    void nothing() {
        given(numberGenerator.generate(3, 1, 9)).willReturn(convert("145"));

        sut.start();

        String result = sut.answer(convert("678"));

        assertThat(result).isEqualTo("nothing");
    }

    @Test
    void has_number_application() {
        given(numberGenerator.generate(3, 1, 9)).willReturn(convert("145"));

        sut.start();

        List<Integer> numbers = sut.getNumbers();

        assertThat(numbers.size()).isEqualTo(3);
    }

    @Test
    void ball() {
        given(numberGenerator.generate(3, 1, 9)).willReturn(convert("145"));

        sut.start();

        String result = sut.answer(convert("991"));
        String result2 = sut.answer(convert("491"));
        String result3 = sut.answer(convert("451"));

        assertThat(result).isEqualTo("1볼");
        assertThat(result2).isEqualTo("2볼");
        assertThat(result3).isEqualTo("3볼");
    }

    @Test
    void strike() {
        given(numberGenerator.generate(3, 1, 9)).willReturn(convert("145"));

        sut.start();

        String result = sut.answer(convert("199"));
        String result2 = sut.answer(convert("149"));
        String result3 = sut.answer(convert("145"));

        assertThat(result).isEqualTo("1스트라이크");
        assertThat(result2).isEqualTo("2스트라이크");
        assertThat(result3).isEqualTo("3스트라이크");
    }

    @Test
    void strike_and_ball() {
        given(numberGenerator.generate(3, 1, 9)).willReturn(convert("145"));

        sut.start();

        String result = sut.answer(convert("194"));
        String result2 = sut.answer(convert("154"));
        String result3 = sut.answer(convert("415"));

        assertThat(result).isEqualTo("1스트라이크 1볼");
        assertThat(result2).isEqualTo("1스트라이크 2볼");
        assertThat(result3).isEqualTo("1스트라이크 2볼");
    }
}
