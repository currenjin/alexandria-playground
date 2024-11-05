package baseball.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BallsTest {

    @Test
    void play_result_nothing() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));
        PlayResult result = balls.play(Lists.newArrayList(4, 5, 6));

        assertThat(result.getStrike()).isEqualTo(0);
        assertThat(result.getBall()).isEqualTo(0);
    }

    @Test
    void play_result_1ball() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));
        PlayResult result = balls.play(Lists.newArrayList(4, 3, 6));

        assertThat(result.getStrike()).isEqualTo(0);
        assertThat(result.getBall()).isEqualTo(1);
    }

    @Test
    void play_result_1strike() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));
        PlayResult result = balls.play(Lists.newArrayList(1, 5, 6));

        assertThat(result.getStrike()).isEqualTo(1);
        assertThat(result.getBall()).isEqualTo(0);
    }

    @Test
    void play_result_1strike_1ball() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));
        PlayResult result = balls.play(Lists.newArrayList(1, 3, 6));

        assertThat(result.getStrike()).isEqualTo(1);
        assertThat(result.getBall()).isEqualTo(1);
        assertThat(result.isGameEnd()).isFalse();
    }

    @Test
    void play_result_3strike() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));
        PlayResult result = balls.play(Lists.newArrayList(1, 2, 3));

        assertThat(result.getStrike()).isEqualTo(3);
        assertThat(result.getBall()).isEqualTo(0);
        assertThat(result.isGameEnd()).isTrue();
    }

    @Test
    void nothing() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));

        assertThat(balls.play(new Ball(1, 4))).isEqualTo(BallStatus.NOTHING);
    }

    @Test
    void ball() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));

        assertThat(balls.play(new Ball(2, 1))).isEqualTo(BallStatus.BALL);
    }

    @Test
    void strike() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));

        assertThat(balls.play(new Ball(1, 1))).isEqualTo(BallStatus.STRIKE);
    }
}
