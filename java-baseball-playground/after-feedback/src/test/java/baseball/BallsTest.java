package baseball;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BallsTest {

    @Test
    void play_result_0strike_0ball() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));
        PlayResult result = balls.play(Lists.newArrayList(6, 4, 5));

        assertThat(result.getStrike()).isEqualTo(0);
        assertThat(result.getBall()).isEqualTo(0);
    }

    @Test
    void play_result_1strike_0ball() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));
        PlayResult result = balls.play(Lists.newArrayList(1, 4, 5));

        assertThat(result.getStrike()).isEqualTo(1);
        assertThat(result.getBall()).isEqualTo(0);
    }

    @Test
    void play_result_1strike_2ball() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));
        PlayResult result = balls.play(Lists.newArrayList(1, 3, 2));

        assertThat(result.getStrike()).isEqualTo(1);
        assertThat(result.getBall()).isEqualTo(2);
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
        assertThat(balls.play(new Ball(1, 4))).isEqualTo(BaseballStatus.NOTHING);
    }

    @Test
    void ball() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));
        assertThat(balls.play(new Ball(1, 2))).isEqualTo(BaseballStatus.BALL);
    }

    @Test
    void strike() {
        Balls balls = new Balls(Lists.newArrayList(1, 2, 3));
        assertThat(balls.play(new Ball(1, 1))).isEqualTo(BaseballStatus.STRIKE);
    }
}
