package domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarTest {
    @Test
    @DisplayName("자동차의 이름을 지정할 수 있다")
    void car_name() {
        Car actual = new Car("first");

        assertEquals("first", actual.getName());
    }

    @Test
    @DisplayName("자동차의 초기 위치는 0이다")
    void car_init_position() {
        Car actual = new Car("first");

        assertEquals(0, actual.getPosition());
    }

    @Test
    @DisplayName("자동차가 1만큼 움직이면 포지션은 1이 추가된다")
    void car_move_1_position() {
        Car actual = new Car("first");
        assertEquals(0, actual.getPosition());

        actual.move(1);

        assertEquals(1, actual.getPosition());
    }

    @Test
    @DisplayName("자동차가 2만큼 움직이면 포지션은 2가 추가된다")
    void car_move_2_position() {
        Car actual = new Car("first");
        assertEquals(0, actual.getPosition());

        actual.move(2);

        assertEquals(2, actual.getPosition());
    }
}