package baseball;

public class PlayResult {
    private int strike = 0;
    private int ball = 0;

    public int getStrike() {
        return this.strike;
    }

    public int getBall() {
        return this.ball;
    }

    public void report(BaseballStatus baseballStatus) {
        if (baseballStatus.isStrike()) {
            this.strike += 1;
        }

        if (baseballStatus.isBall()) {
            this.ball += 1;
        }
    }

    public boolean isGameEnd() {
        return this.strike == 3;
    }
}
