package com.tdd;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/* 기능 요구사항
1. 쉼표(,) 또는 콜론(:)을 구분자로 가지는 문자열을 전달하는 경우 구분자를 기준으로 분리한 각 숫자의 합을 반환
    (예: “” => 0, "1,2" => 3, "1,2,3" => 6, “1,2:3” => 6)

2. 앞의 기본 구분자(쉼표, 콜론)외에 커스텀 구분자를 지정할 수 있다.
    커스텀 구분자는 문자열 앞부분의 “//”와 “\n” 사이에 위치하는 문자를 커스텀 구분자로 사용한다.
    예를 들어 “//;\n1;2;3”과 같이 값을 입력할 경우 커스텀 구분자는 세미콜론(;)이며, 결과 값은 6이 반환되어야 한다.

3. 문자열 계산기에 숫자 이외의 값 또는 음수를 전달하는 경우 RuntimeException 예외를 throw한다.
 */


// TODO: 커스텀 구분자
public class StringCalculatorTest {
    @Test
    void empty_is_zero() {
        Integer actual = StringCalculator.calculate("");

        assertThat(actual).isEqualTo(0);
    }

    @Test
    void null_is_zero() {
        Integer actual = StringCalculator.calculate(null);

        assertThat(actual).isEqualTo(0);
    }

    @Test
    void one_is_one() {
        Integer actual = StringCalculator.calculate("1");

        assertThat(actual).isEqualTo(1);
    }

    @Test
    void one_comma_one_is_two() {
        Integer actual = StringCalculator.calculate("1,1");

        assertThat(actual).isEqualTo(2);
    }

    @Test
    void one_comma_two_is_three() {
        Integer actual = StringCalculator.calculate("1,2");

        assertThat(actual).isEqualTo(3);
    }

    @Test
    void comma_two_is_two() {
        Integer actual = StringCalculator.calculate(",2");

        assertThat(actual).isEqualTo(2);
    }

    @Test
    void colons_two_is_two() {
        Integer actual = StringCalculator.calculate(":2");

        assertThat(actual).isEqualTo(2);
    }

    @Test
    void one_colons_two_comma_three_is_six() {
        Integer actual = StringCalculator.calculate("1:2,3");

        assertThat(actual).isEqualTo(6);
    }

    @Test
    void one_colons_two_comma_thirty_three_is_thirty_six() {
        Integer actual = StringCalculator.calculate("1:2,33");

        assertThat(actual).isEqualTo(36);
    }

    @Test
    void one_custom_separator_two_is_three() {
        Integer actual = StringCalculator.calculate("1//[\n2");

        assertThat(actual).isEqualTo(3);
    }

//    TODO: 잘못된 걸 깨달았다. 맨 앞에 구분자를 정의하고 계속 쓰는 거였다
//    @Test
//    void custom_separator_one_colons_two_colons_is_six() {
//        Integer actual = StringCalculator.calculate("//;\n1;2;3");
//
//        assertThat(actual).isEqualTo(6);
//    }

    @Test
    void has_a_non_integer_is_exception() {
        assertThatThrownBy(() -> StringCalculator.calculate("test"));
    }

    @Test
    void minus_integer_is_exception() {
        assertThatThrownBy(() -> StringCalculator.calculate("-1:231"));
    }
}
