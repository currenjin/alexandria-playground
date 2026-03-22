package com.currenjin;

import java.util.HashMap;
import java.util.Map;

public class Solution {
    public String solution(String[] participant, String[] completion) {
        Map<String, Integer> counts = new HashMap<>();
        for (String name : participant) counts.put(name, counts.getOrDefault(name, 0) + 1);
        for (String name : completion) counts.put(name, counts.getOrDefault(name, 0) - 1);
        for (Map.Entry<String, Integer> e : counts.entrySet()) if (e.getValue() > 0) return e.getKey();
        return "";
    }
}
