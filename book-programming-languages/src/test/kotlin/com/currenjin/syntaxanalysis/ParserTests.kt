package com.currenjin.syntaxanalysis

import com.currenjin.syntaxanalysis.lexer.Lexer
import com.currenjin.syntaxanalysis.parser.Expr
import com.currenjin.syntaxanalysis.parser.Parser
import com.currenjin.syntaxanalysis.parser.Stmt
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ParserTests {
    private fun parse(code: String) = Parser(Lexer(code).tokenize()).program()

    @Test
    fun `단일 대입문 파싱`() {
        val p = parse("x = 10;")
        assertEquals(1, p.statements.size)
        val s = p.statements.first()
        assertTrue(s is Stmt.Assign)
        val a = s as Stmt.Assign
        assertEquals("x", a.name)
        assertEquals(Expr.Number(10), a.expr)
    }

    @Test
    fun `print 문 파싱`() {
        val p = parse("print 2 + 3;")
        val s = p.statements.first()
        assertTrue(s is Stmt.Print)
        val e = (s as Stmt.Print).expr
        assertTrue(e is Expr.Binary)
    }

    @Test
    fun `우선순위 - 곱셈이 덧셈보다 높다`() {
        val p = parse("x = 2 + 3 * 4;")
        val e = (p.statements.first() as Stmt.Assign).expr
        assertTrue(e is Expr.Binary) // 최상위는 +
        val plus = e as Expr.Binary
        assertEquals("+", plus.op)
        assertTrue(plus.right is Expr.Binary) // 우항은 (3 * 4)
        val mul = plus.right as Expr.Binary
        assertEquals("*", mul.op)
    }

    @Test
    fun `괄호가 우선순위를 바꾼다`() {
        val p = parse("x = (2 + 3) * 4;")
        val e = (p.statements.first() as Stmt.Assign).expr
        assertTrue(e is Expr.Binary)
        val mul = e as Expr.Binary
        assertEquals("*", mul.op)
        // 좌항이 (2+3)
        assertTrue(mul.left is Expr.Paren)
        val paren = mul.left as Expr.Paren
        assertTrue(paren.expr is Expr.Binary)
        assertEquals("+", (paren.expr as Expr.Binary).op)
    }

    @Test
    fun `단항 마이너스`() {
        val p = parse("x = -5 + 2;")
        val e = (p.statements.first() as Stmt.Assign).expr
        assertTrue(e is Expr.Binary)
        val plus = e as Expr.Binary
        assertEquals("+", plus.op)
        assertTrue(plus.left is Expr.Unary)
        assertEquals("-", (plus.left as Expr.Unary).op)
    }

    @Test
    fun `에러 - 세미콜론 누락`() {
        val ex =
            assertThrows(IllegalStateException::class.java) {
                parse("x = 1")
            }
        assertTrue(ex.message!!.contains("Missing ';'"))
    }
}
