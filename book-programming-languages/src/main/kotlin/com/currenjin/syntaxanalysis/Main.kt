package com.currenjin.syntaxanalysis

import com.currenjin.syntaxanalysis.lexer.Lexer
import com.currenjin.syntaxanalysis.parser.Parser
import com.currenjin.syntaxanalysis.runtime.Evaluator

fun main() {
    println("Mini Language (BNF → AST → Eval)")
    println("입력 예: x = 2 + 3 * (4 + 5); print x;")

    while (true) {
        print("> ")
        val line = readlnOrNull() ?: break
        if (line.isBlank()) break

        try {
            val tokens = Lexer(line).tokenize()
            val program = Parser(tokens).program()
            val result = Evaluator().run(program)
            result.forEach(::println)
        } catch (e: Exception) {
            System.err.println("Error: ${e.message}")
        }
    }
}
