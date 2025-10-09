package com.currenjin.syntaxanalysis.lexer

class Lexer(
    private val src: String,
) {
    private var pos = 0

    private fun isAtEnd() = pos >= src.length

    private fun peek() = if (isAtEnd()) '\u0000' else src[pos]

    private fun advance() = src[pos++]

    private fun skipWhitespace() {
        while (!isAtEnd() && peek().isWhitespace()) pos++
    }

    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()
        while (true) {
            skipWhitespace()
            if (isAtEnd()) {
                tokens.add(Token(TokenType.EOF, "", pos))
                break
            }
            val start = pos
            when (val c = peek()) {
                '+' -> {
                    advance()
                    tokens.add(Token(TokenType.PLUS, "+", start))
                }
                '-' -> {
                    advance()
                    tokens.add(Token(TokenType.MINUS, "-", start))
                }
                '*' -> {
                    advance()
                    tokens.add(Token(TokenType.STAR, "*", start))
                }
                '/' -> {
                    advance()
                    tokens.add(Token(TokenType.SLASH, "/", start))
                }
                '(' -> {
                    advance()
                    tokens.add(Token(TokenType.LPAREN, "(", start))
                }
                ')' -> {
                    advance()
                    tokens.add(Token(TokenType.RPAREN, ")", start))
                }
                '=' -> {
                    advance()
                    tokens.add(Token(TokenType.EQUAL, "=", start))
                }
                ';' -> {
                    advance()
                    tokens.add(Token(TokenType.SEMICOLON, ";", start))
                }
                else ->
                    when {
                        c.isDigit() -> tokens.add(number())
                        c.isLetter() || c == '_' -> tokens.add(identifier())
                        else -> throw IllegalArgumentException("Unexpected char '$c' at $pos")
                    }
            }
        }
        return tokens
    }

    private fun number(): Token {
        val start = pos
        while (!isAtEnd() && peek().isDigit()) advance()
        val lexeme = src.substring(start, pos)
        return Token(TokenType.NUMBER, lexeme, start)
    }

    private fun identifier(): Token {
        val start = pos
        while (!isAtEnd() && (peek().isLetterOrDigit() || peek() == '_')) advance()
        val lexeme = src.substring(start, pos)
        return if (lexeme == "print") {
            Token(TokenType.PRINT, lexeme, start)
        } else {
            Token(TokenType.IDENT, lexeme, start)
        }
    }
}
