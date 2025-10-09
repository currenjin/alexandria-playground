package com.currenjin.syntaxanalysis.parser

sealed interface Expr {
    data class Binary(
        val left: Expr,
        val op: String,
        val right: Expr,
    ) : Expr

    data class Unary(
        val op: String,
        val expr: Expr,
    ) : Expr

    data class Number(
        val value: Long,
    ) : Expr

    data class Variable(
        val name: String,
    ) : Expr

    data class Paren(
        val expr: Expr,
    ) : Expr
}

sealed interface Stmt {
    data class Print(
        val expr: Expr,
    ) : Stmt

    data class Assign(
        val name: String,
        val expr: Expr,
    ) : Stmt
}

data class Program(
    val statements: List<Stmt>,
)
