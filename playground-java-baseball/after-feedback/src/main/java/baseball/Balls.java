package baseball;

import java.util.ArrayList;
import java.util.List;

public class Balls {
    private final List<Ball> balls;

    public Balls(List<Integer> answers) {
        this.balls = mapBall(answers);
    }

    private List<Ball> mapBall(List<Integer> answers) {
        List<Ball> balls = new ArrayList<>();

        for (int i = 0; i < answers.size(); i++) {
            balls.add(new Ball(i + 1, answers.get(i)));
        }
        return balls;
    }

    public BaseballStatus play(Ball userBall) {
        return balls.stream()
                .map(ball -> ball.play(userBall))
                .filter(status -> status.isNotNothing())
                .findFirst()
                .orElse(BaseballStatus.NOTHING);
    }

    public PlayResult play(List<Integer> userBall) {
        Balls userBalls = new Balls(userBall);
        PlayResult playResult = new PlayResult();

        balls.forEach(ball -> {
            BaseballStatus baseballStatus = userBalls.play(ball);
            playResult.report(baseballStatus);
        });

        return playResult;
    }
}
