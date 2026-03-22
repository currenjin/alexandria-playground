package com.currenjin

import java.util.ArrayDeque

class KotlinSolution {
    fun solution(maps: Array<IntArray>): Int {
        val n = maps.size
        val m = maps[0].size
        val dist = Array(n) { IntArray(m) }
        val dr = intArrayOf(1, -1, 0, 0)
        val dc = intArrayOf(0, 0, 1, -1)
        val q = ArrayDeque<Pair<Int, Int>>()
        q.add(0 to 0)
        dist[0][0] = 1

        while (q.isNotEmpty()) {
            val (r, c) = q.removeFirst()
            for (k in 0..3) {
                val nr = r + dr[k]
                val nc = c + dc[k]
                if (nr !in 0 until n || nc !in 0 until m) continue
                if (maps[nr][nc] == 0 || dist[nr][nc] != 0) continue
                dist[nr][nc] = dist[r][c] + 1
                q.add(nr to nc)
            }
        }
        return if (dist[n - 1][m - 1] == 0) -1 else dist[n - 1][m - 1]
    }
}
