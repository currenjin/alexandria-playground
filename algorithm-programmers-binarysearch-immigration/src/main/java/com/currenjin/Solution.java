package com.currenjin;

public class Solution {
    public long solution(int n, int[] times) {
        long left = 1L;
        long right = (long) max(times) * n;
        long answer = right;

        while (left <= right) {
            long mid = (left + right) / 2;
            long processed = 0L;
            for (int t : times) processed += mid / t;

            if (processed >= n) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return answer;
    }

    private int max(int[] arr) {
        int m = arr[0];
        for (int x : arr) if (x > m) m = x;
        return m;
    }
}
