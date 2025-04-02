package baseball;

import java.util.List;

public class Validator {
    private static int MIN_NO = 0;
    private static int MAX_NO = 10;

    public static Boolean validNo(int number) {
        return number > MIN_NO && number < MAX_NO;
    }

    public static Boolean validSize(Integer size, List<Integer> numbers) {
        return size.equals(numbers.size());
    }

    public static Boolean validNotDuplicate(List<Integer> numbers) {
        return numbers.size() == numbers.stream().distinct().count();
    }
}
