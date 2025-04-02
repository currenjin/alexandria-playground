package baseball.util;

import java.util.ArrayList;
import java.util.List;

public class NumberConverter {

    public static List<Integer> convert(String string) {
        String[] strings = string.split("");
        List<Integer> numbers = new ArrayList<>();

        for (String s : strings) {
            numbers.add(Integer.parseInt(s));
        }
        
        return numbers;
    }
}
