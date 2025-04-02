package baseball;

public enum BaseballStatus {
    BALL, STRIKE, NOTHING;

    public boolean isStrike() {
        return this == BaseballStatus.STRIKE;
    }

    public boolean isBall() {
        return this == BaseballStatus.BALL;
    }

    public boolean isNotNothing() {
        return this != BaseballStatus.NOTHING;
    }
}
