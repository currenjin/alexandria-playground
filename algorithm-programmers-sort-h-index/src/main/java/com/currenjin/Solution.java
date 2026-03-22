package com.currenjin;

import java.util.Arrays;

public class Solution {
    public int solution(int[] citations) {
        Integer[] sorted = Arrays.stream(citations).boxed().toArray(Integer[]::new);
        Arrays.sort(sorted, (a, b) -> b - a);
        int h = 0;
        for (int i = 0; i < sorted.length; i++) {
            int candidate = i + 1;
            if (sorted[i] >= candidate) h = candidate;
            else break;
        }
        return h;
    }
}
