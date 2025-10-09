package com.currenjin.syntaxanalysis.runtime

import com.currenjin.syntaxanalysis.parser.Expr
import com.currenjin.syntaxanalysis.parser.Program
import com.currenjin.syntaxanalysis.parser.Stmt

class Evaluator {
    private val env = mutableMapOf<String, Long>()

    fun run(program: Program): List<String> {
        val outputs = mutableListOf<String>()
        for (stmt in program.statements) {
            when (stmt) {
                is Stmt.Assign -> env[stmt.name] = eval(stmt.expr)
                is Stmt.Print -> outputs.add(eval(stmt.expr).toString())
            }
        }
        return outputs
    }

    private fun eval(expr: Expr): Long =
        when (expr) {
            is Expr.Number -> expr.value
            is Expr.Variable -> env[expr.name] ?: error("Undefined variable '${expr.name}'")
            is Expr.Unary ->
                when (expr.op) {
                    "-" -> -eval(expr.expr)
                    else -> error("Unknown unary ${expr.op}")
                }
            is Expr.Binary -> {
                val l = eval(expr.left)
                val r = eval(expr.right)
                when (expr.op) {
                    "+" -> l + r
                    "-" -> l - r
                    "*" -> l * r
                    "/" -> l / r
                    else -> error("Unknown op ${expr.op}")
                }
            }
            is Expr.Paren -> eval(expr.expr)
        }
}
