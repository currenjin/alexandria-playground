package com.currenjin;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public int[] solution(int[] progresses, int[] speeds) {
        int[] days = new int[progresses.length];
        for (int i = 0; i < progresses.length; i++) {
            int remain = 100 - progresses[i];
            days[i] = (remain + speeds[i] - 1) / speeds[i];
        }

        List<Integer> result = new ArrayList<>();
        int current = days[0];
        int count = 1;

        for (int i = 1; i < days.length; i++) {
            if (days[i] <= current) count++;
            else {
                result.add(count);
                current = days[i];
                count = 1;
            }
        }
        result.add(count);

        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}
