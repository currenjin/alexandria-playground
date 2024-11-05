package baseball.domain;

public enum BallStatus {
    BALL, STRIKE, NOTHING;

    public boolean isNotNothing() {
        return this != BallStatus.NOTHING;
    }

    public boolean isStrike() {
        return this == BallStatus.STRIKE;
    }

    public boolean isBall() {
        return this ==  BallStatus.BALL;
    }
}
