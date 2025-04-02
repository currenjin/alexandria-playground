package study;

import java.util.ArrayList;
import java.util.List;

public class NumbersConverter {
    public static List<Integer> convert(String string) {
        String[] strings = string.split("");
        List<Integer> result = new ArrayList<>();

        for (String s : strings) {
            result.add(Integer.parseInt(s));
        }

        return result;
    }
}
