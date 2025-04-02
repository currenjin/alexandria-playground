package baseball.util;

import java.util.List;

public class Validator {
    public boolean validNumber(int number) {
        return number > 0 && number < 10;
    }

    public boolean isNotDuplicateNumber(List<Integer> numbers) {
        return numbers.size() == numbers.stream().distinct().count();
    }

    public boolean validSize(int size, List<Integer> numbers) {
        return size == numbers.size();
    }
}
