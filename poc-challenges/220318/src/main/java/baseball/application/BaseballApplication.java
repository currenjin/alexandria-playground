package baseball.application;

import baseball.domain.Balls;
import baseball.domain.PlayResult;
import baseball.util.NumberConverter;
import baseball.util.RandomNumber;
import baseball.util.Validator;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class BaseballApplication {
    public static void main(String[] args) {
        Balls balls = new Balls(RandomNumber.generate(3, 1, 9));
        Boolean start = true;

        while (start) {
            System.out.print("숫자를 입력하세요 : ");
            List<Integer> numbers = NumberConverter.convert(new Scanner(System.in).nextLine());

            if (!validate(numbers)) continue;

            PlayResult playResult = balls.play(numbers);

            System.out.println(String.format("%s 스트라이크 %s 볼", playResult.getStrike(), playResult.getBall()));
            start = isStart(playResult);
            balls = initBalls(playResult, balls);
        }
    }

    private static Boolean validate(List<Integer> numbers) {
        Validator validator = new Validator();

        if (!validator.validSize(3, numbers)) {
            System.out.println("세 자리의 수를 입력해 주세요. 현재 입력 개수 : " + numbers.size());
            return false;
        }

        if (!validator.isNotDuplicateNumber(numbers)) {
            System.out.println("중복되는 수가 있습니다.");
            return false;
        }

        Stream<Integer> filteredNumbers = numbers.stream().filter(n -> !validator.validNumber(n));

        if (filteredNumbers.findFirst().isPresent()) {
            System.out.println("1 ~ 9 사이의 수를 입력해 주세요.");
            return false;
        }

        return true;
    }

    private static Balls initBalls(PlayResult playResult, Balls balls) {
        if (playResult.isGameEnd()) {
            balls = new Balls(RandomNumber.generate(3, 1, 9));
        }
        return balls;
    }

    private static boolean isStart(PlayResult playResult) {
        if (playResult.isGameEnd()) {
            return isRestart();
        }

        return !playResult.isGameEnd();
    }

    private static boolean isRestart() {
        System.out.println("정답입니다! 다시 시작하시겠습니까?");
        System.out.println("재시작 : 1");
        System.out.println("종료 : 2");

        Scanner scanner = new Scanner(System.in);
        Integer number = Integer.parseInt(scanner.nextLine());

        if (number.equals(2)) {
            return false;
        }

        return true;
    }
}
