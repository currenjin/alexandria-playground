package baseball.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BallTest {

    @Test
    void nothing() {
        Ball computer = new Ball(1, 1);
        assertThat(computer.play(new Ball(1, 4))).isEqualTo(BallStatus.NOTHING);
    }

    @Test
    void ball() {
        Ball computer = new Ball(1, 1);
        assertThat(computer.play(new Ball(2, 1))).isEqualTo(BallStatus.BALL);
    }

    @Test
    void strike() {
        Ball computer = new Ball(1, 1);
        assertThat(computer.play(new Ball(1, 1))).isEqualTo(BallStatus.STRIKE);
    }
}
