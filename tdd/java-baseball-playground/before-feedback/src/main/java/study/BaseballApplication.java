package study;

import java.util.List;

public class BaseballApplication {
    private List<Integer> numbers;
    private final NumberGenerator numberGenerator;

    public BaseballApplication(NumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    public void start() {
        this.numbers = this.numberGenerator.generate(3, 1, 9);
    }

    public String answer(List<Integer> input) {
        return resultCalculator(input);
    }

    private String resultCalculator(List<Integer> input) {
        String strike = striker(input);
        String ball = baller(input);

        if (!strike.isEmpty() && !ball.isEmpty()) return strike + " " + ball;
        if (strike.isEmpty() && ball.isEmpty()) return "nothing";
        return strike + ball;
    }

    private String striker(List<Integer> input) {
        int count = 0;

        for (int i=0; i<input.size(); i++) {
            count += strikeExplorer(input.get(i), numbers.get(i));
        }

        if (count > 0) return count + "스트라이크";
        return "";
    }

    private String baller(List<Integer> input) {
        int count = 0;

        for (int i=0; i<input.size(); i++) {
            count += ballExplorer(input.get(i), numbers.get(i));
        }

        if (count > 0) return count + "볼";
        return "";
    }

    private int strikeExplorer(Integer number1, Integer number2) {
        if (number1.equals(number2)) return 1;
        return 0;
    }

    private int ballExplorer(Integer integer1, Integer integer2) {
        if (numbers.contains(integer1) && !integer1.equals(integer2)) return 1;
        return 0;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }
}
