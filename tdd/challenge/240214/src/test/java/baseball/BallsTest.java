package baseball;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class BallsTest {

    public static final Balls answers = new Balls(Arrays.asList(1, 2, 3));

    @Test
    void play_nothing() {
        PlayResult result = answers.play(Arrays.asList(4, 5, 6));

        assertThat(result.getStrike()).isEqualTo(0);
        assertThat(result.getBall()).isEqualTo(0);
    }

    @Test
    void play_1strike_1ball() {
        PlayResult result = answers.play(Arrays.asList(1, 4, 2));

        assertThat(result.getStrike()).isEqualTo(1);
        assertThat(result.getBall()).isEqualTo(1);
    }

    @Test
    void play_3ball() {
        PlayResult result = answers.play(Arrays.asList(3, 1, 2));

        assertThat(result.getStrike()).isEqualTo(0);
        assertThat(result.getBall()).isEqualTo(3);
    }

    @Test
    void play_3strike() {
        PlayResult result = answers.play(Arrays.asList(1, 2, 3));

        assertThat(result.getStrike()).isEqualTo(3);
        assertThat(result.getBall()).isEqualTo(0);
    }

    @Test
    void play_game_end() {
        PlayResult result = answers.play(Arrays.asList(1, 2, 3));

        assertThat(result.getStrike()).isEqualTo(3);
        assertThat(result.getBall()).isEqualTo(0);
        assertThat(result.isGameEnd()).isTrue();
    }

    @Test
    void strike() {
        BallStatus status = answers.play(new Ball(1, 1));

        assertThat(status).isEqualTo(BallStatus.STRIKE);
    }

    @Test
    void ball() {
        BallStatus status = answers.play(new Ball(1, 2));

        assertThat(status).isEqualTo(BallStatus.BALL);
    }

    @Test
    void nothing() {
        BallStatus status = answers.play(new Ball(1, 4));

        assertThat(status).isEqualTo(BallStatus.NOTHING);
    }
}
