package study;

import java.util.List;
import java.util.Scanner;

public class BaseballGameController {
    public static void main(String[] args) {
        NumberGenerator numberGenerator = new NumberGenerator();
        BaseballApplication baseballApplication = new BaseballApplication(numberGenerator);
        boolean start = true;

        while (start) {
            gameStart(baseballApplication);
            System.out.println("3개의 숫자를 모두 맞히셨습니다! 게임 종료");
            if (isNotRestart()) break;
        }
    }

    private static boolean isNotRestart() {
        System.out.println("게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요.");
        Scanner scanner = new Scanner(System.in);

        if (scanner.nextLine().equals("0")) return true;
        return false;
    }

    private static void gameStart(BaseballApplication baseballApplication) {
        String result = "";

        baseballApplication.start();

        while (!result.equals("3스트라이크")) {
            System.out.print("숫자를 입력해 주세요 : ");
            Scanner scanner = new Scanner(System.in);
            List<Integer> numbers = NumbersConverter.convert(scanner.nextLine());
            result = baseballApplication.answer(numbers);
            System.out.println(result);
        }
    }
}
