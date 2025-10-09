package com.currenjin.syntaxanalysis.parser

import com.currenjin.syntaxanalysis.lexer.Token
import com.currenjin.syntaxanalysis.lexer.TokenType

class Parser(
    private val tokens: List<Token>,
) {
    private var pos = 0

    private fun peek() = tokens[pos]

    private fun isAtEnd() = peek().type == TokenType.EOF

    private fun advance() = tokens[pos++]

    private fun match(vararg types: TokenType) =
        types.any {
            if (peek().type == it) {
                advance()
                true
            } else {
                false
            }
        }

    private fun expect(
        type: TokenType,
        msg: String,
    ): Token {
        if (peek().type == type) return advance()
        throw IllegalStateException("$msg at ${peek()}")
    }

    fun program(): Program {
        val stmts = mutableListOf<Stmt>()
        while (!isAtEnd()) stmts.add(stmt())
        return Program(stmts)
    }

    private fun stmt(): Stmt =
        if (match(TokenType.PRINT)) {
            val expr = expr()
            expect(TokenType.SEMICOLON, "Missing ';'")
            Stmt.Print(expr)
        } else {
            val name = expect(TokenType.IDENT, "Expected variable name").lexeme
            expect(TokenType.EQUAL, "Expected '='")
            val expr = expr()
            expect(TokenType.SEMICOLON, "Missing ';'")
            Stmt.Assign(name, expr)
        }

    private fun expr(): Expr {
        var left = term()
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            val op = tokens[pos - 1].lexeme
            val right = term()
            left = Expr.Binary(left, op, right)
        }
        return left
    }

    private fun term(): Expr {
        var left = factor()
        while (match(TokenType.STAR, TokenType.SLASH)) {
            val op = tokens[pos - 1].lexeme
            val right = factor()
            left = Expr.Binary(left, op, right)
        }
        return left
    }

    private fun factor(): Expr =
        when {
            match(TokenType.MINUS) -> Expr.Unary("-", factor())
            match(TokenType.NUMBER) -> Expr.Number(tokens[pos - 1].lexeme.toLong())
            match(TokenType.IDENT) -> Expr.Variable(tokens[pos - 1].lexeme)
            match(TokenType.LPAREN) -> {
                val e = expr()
                expect(TokenType.RPAREN, "Expected ')'")
                Expr.Paren(e)
            }
            else -> throw IllegalStateException("Unexpected token ${peek()}")
        }
}
