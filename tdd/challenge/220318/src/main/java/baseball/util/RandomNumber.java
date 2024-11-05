package baseball.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomNumber {
    public static List<Integer> generate(int size, int start, int end) {
        Set<Integer> numbers = new HashSet<>();

        while (numbers.size() < size) {
            Double d = Math.random() * end + start;
            numbers.add(d.intValue());
        }

        return new ArrayList<>(numbers);
    }
}
