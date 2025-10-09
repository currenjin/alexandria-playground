package com.currenjin.syntaxanalysis

import com.currenjin.syntaxanalysis.lexer.Lexer
import com.currenjin.syntaxanalysis.lexer.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LexerTests {
    private fun typesOf(src: String) = Lexer(src).tokenize().map { it.type }

    @Test
    fun `토큰화 - 기본 식과 대입문`() {
        val t = typesOf("x = 2 + 3 * (4 + 5);")
        assertEquals(
            listOf(
                TokenType.IDENT,
                TokenType.EQUAL,
                TokenType.NUMBER,
                TokenType.PLUS,
                TokenType.NUMBER,
                TokenType.STAR,
                TokenType.LPAREN,
                TokenType.NUMBER,
                TokenType.PLUS,
                TokenType.NUMBER,
                TokenType.RPAREN,
                TokenType.SEMICOLON,
                TokenType.EOF,
            ),
            t,
        )
    }

    @Test
    fun `키워드 - print 인식`() {
        val t = typesOf("print 10;")
        assertEquals(TokenType.PRINT, t.first())
    }

    @Test
    fun `공백과 줄바꿈 무시`() {
        val t = typesOf(" \n\t  a=1;\nprint a;")
        assertEquals(
            listOf(
                TokenType.IDENT,
                TokenType.EQUAL,
                TokenType.NUMBER,
                TokenType.SEMICOLON,
                TokenType.PRINT,
                TokenType.IDENT,
                TokenType.SEMICOLON,
                TokenType.EOF,
            ),
            t,
        )
    }

    @Test
    fun `에러 - 알 수 없는 문자`() {
        val ex =
            assertThrows(IllegalArgumentException::class.java) {
                typesOf("@")
            }
        assertTrue(ex.message!!.contains("Unexpected"))
    }
}
