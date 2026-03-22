package com.currenjin;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    public int solution(String[][] clothes) {
        Map<String, Integer> countByKind = new HashMap<>();
        for (String[] cloth : clothes) countByKind.put(cloth[1], countByKind.getOrDefault(cloth[1], 0) + 1);
        int answer = 1;
        for (int count : countByKind.values()) answer *= (count + 1);
        return answer - 1;
    }
}
