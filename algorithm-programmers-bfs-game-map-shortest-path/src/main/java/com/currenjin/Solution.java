package com.currenjin;

import java.util.ArrayDeque;
import java.util.Queue;

public class Solution {
    public int solution(int[][] maps) {
        int n = maps.length;
        int m = maps[0].length;
        int[][] dist = new int[n][m];
        int[] dr = {1, -1, 0, 0};
        int[] dc = {0, 0, 1, -1};

        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{0, 0});
        dist[0][0] = 1;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1];

            for (int k = 0; k < 4; k++) {
                int nr = r + dr[k], nc = c + dc[k];
                if (nr < 0 || nr >= n || nc < 0 || nc >= m) continue;
                if (maps[nr][nc] == 0 || dist[nr][nc] != 0) continue;
                dist[nr][nc] = dist[r][c] + 1;
                q.offer(new int[]{nr, nc});
            }
        }

        return dist[n - 1][m - 1] == 0 ? -1 : dist[n - 1][m - 1];
    }
}
