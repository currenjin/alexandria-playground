package com.currenjin

class KotlinSolution {
    fun solution(people: IntArray, limit: Int): Int {
        people.sort()
        var left = 0
        var right = people.lastIndex
        var boats = 0
        while (left <= right) {
            if (people[left] + people[right] <= limit) left++
            right--
            boats++
        }
        return boats
    }
}
