package com.tdd;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringCalculator {
    public static Integer calculate(String string) {
        if (checkNullOrEmpty(string)) {
            return 0;
        }

        List<Integer> numbers = splitNumbers(string);

        if (numbers.size() == 1) {
            return numbers.get(0);
        }

        return numbers.stream().reduce(0, Integer::sum);
    }

    private static List<Integer> splitNumbers(String string) {
        List<String> splitedStringList = splitBySeparator(string);
        return splitedStringList
                .stream()
                .map(e -> {
                    if (checkNullOrEmpty(e)) return 0;
                    if (isMinusNumber(e)) throw new IllegalArgumentException();
                    return Integer.parseInt(e);
                })
                .collect(Collectors.toList());
    }

    private static List<String> splitBySeparator(String string) {
        String replacedString = string.replaceAll("//(.*?)\n", ",");
        return Arrays.asList(replacedString.split("[,:]"));
    }

    private static Boolean checkNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private static Boolean isMinusNumber(String s) {
        return Integer.parseInt(s) < 0;
    }
}
