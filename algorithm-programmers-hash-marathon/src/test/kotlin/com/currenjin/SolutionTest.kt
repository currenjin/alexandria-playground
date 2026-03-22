package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SolutionTest {
    @Test
    fun sample1() {
        val participant = arrayOf("leo", "kiki", "eden")
        val completion = arrayOf("eden", "kiki")
        assertEquals("leo", Solution().solution(participant, completion))
    }

    @Test
    fun duplicateName() {
        val participant = arrayOf("mislav", "stanko", "mislav", "ana")
        val completion = arrayOf("stanko", "ana", "mislav")
        assertEquals("mislav", Solution().solution(participant, completion))
    }
}
