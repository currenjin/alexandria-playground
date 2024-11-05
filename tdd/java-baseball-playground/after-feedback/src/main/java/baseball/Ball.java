package baseball;

import java.util.Objects;

public class Ball {
    private final int position;
    private final int ballNo;

    public Ball(int position, int ballNo) {
        Validator.validNo(ballNo);

        this.position = position;
        this.ballNo = ballNo;
    }

    public BaseballStatus play(Ball ball) {
        if (this.equals(ball)) {
            return BaseballStatus.STRIKE;
        }

        if (matchedNo(ball.ballNo)) {
            return BaseballStatus.BALL;
        }

        return BaseballStatus.NOTHING;
    }

    private boolean matchedNo(int no) {
        return no == this.ballNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ball ball = (Ball) o;
        return position == ball.position && ballNo == ball.ballNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, ballNo);
    }
}
