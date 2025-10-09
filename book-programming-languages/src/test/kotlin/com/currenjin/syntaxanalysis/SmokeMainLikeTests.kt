package com.currenjin.syntaxanalysis

import com.currenjin.syntaxanalysis.lexer.Lexer
import com.currenjin.syntaxanalysis.parser.Parser
import com.currenjin.syntaxanalysis.runtime.Evaluator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SmokeMainLikeTests {
    @Test
    fun `한 줄 입력을 즉시 실행`() {
        val line = "x = 2 + 3 * (4 + 5); print x;"
        val tokens = Lexer(line).tokenize()
        val program = Parser(tokens).program()
        val out = Evaluator().run(program)
        assertEquals(listOf("29"), out)
    }
}
