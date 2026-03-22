package com.currenjin;

public class Solution {
    public int solution(int[] numbers, int target) {
        return dfs(numbers, 0, 0, target);
    }

    private int dfs(int[] numbers, int idx, int sum, int target) {
        if (idx == numbers.length) return sum == target ? 1 : 0;
        return dfs(numbers, idx + 1, sum + numbers[idx], target)
                + dfs(numbers, idx + 1, sum - numbers[idx], target);
    }
}
