package com.currenjin.syntaxanalysis

import com.currenjin.syntaxanalysis.lexer.Lexer
import com.currenjin.syntaxanalysis.parser.Parser
import com.currenjin.syntaxanalysis.runtime.Evaluator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EvaluatorTests {
    private fun run(code: String): List<String> {
        val tokens = Lexer(code).tokenize()
        val program = Parser(tokens).program()
        return Evaluator().run(program)
    }

    @Test
    fun `산술 연산`() {
        assertEquals(listOf("7"), run("print 1 + 2 * 3;"))
        assertEquals(listOf("9"), run("print (1 + 2) * 3;"))
    }

    @Test
    fun `대입과 참조`() {
        val out =
            run(
                """
                x = 10;
                y = x * 2 + 5;
                print y;
                """.trimIndent(),
            )
        assertEquals(listOf("25"), out)
    }

    @Test
    fun `단항 마이너스 평가`() {
        assertEquals(listOf("-3"), run("print -5 + 2;"))
    }

    @Test
    fun `에러 - 미정의 변수`() {
        val ex =
            assertThrows(IllegalStateException::class.java) {
                run("print z;")
            }
        assertTrue(ex.message!!.contains("Undefined"))
    }

    @Test
    fun `나눗셈과 정수 연산`() {
        assertEquals(listOf("5"), run("print 10 / 2;"))
        assertEquals(listOf("3"), run("print 7 / 2;")) // 정수 나눗셈
    }
}
