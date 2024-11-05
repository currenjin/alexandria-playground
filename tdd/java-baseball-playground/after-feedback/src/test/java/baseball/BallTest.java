package baseball;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BallTest {

    Ball com = new Ball(1, 4);

    @Test
    void nothing() {
        Ball userBall = new Ball(1, 1);
        assertThat(com.play(userBall)).isEqualTo(BaseballStatus.NOTHING);
    }

    @Test
    void ball() {
        Ball userBall = new Ball(2, 4);
        assertThat(com.play(userBall)).isEqualTo(BaseballStatus.BALL);
    }

    @Test
    void strike() {
        Ball userBall = new Ball(1, 4);
        assertThat(com.play(userBall)).isEqualTo(BaseballStatus.STRIKE);
    }
}
