package baseball;

import java.util.ArrayList;
import java.util.List;

public class NumberConverter {
    public static List<Integer> convertBy(String string) {
        List<Integer> numbers = new ArrayList<>();
        String[] strings = string.split("");

        for (String s : strings) {
            numbers.add(Integer.parseInt(s));
        }

        return numbers;
    }
}
