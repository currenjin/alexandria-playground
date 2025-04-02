package com.currenjin.jvm;

import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleJVMInterpreterTest {

    @Test
    void testInterpreterCreation() {
        byte[] bytecode = {0x03};

        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(bytecode);

        assertNotNull(interpreter, "객체가 생성됩니다.");
    }

    @Test
    void testPushConstant() {
        byte[] bytecode = {0x03};
        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(bytecode);

        interpreter.execute();

        Stack<Integer> stack = interpreter.getOperandStack();
        assertEquals(1, stack.size(), "스택에 하나의 요소가 있어야 한다");
        assertEquals(0, stack.peek(), "스택의 최상위 값은 0이어야 한다");
    }

    @Test
    void testPushMultipleConstants() {
        byte[] bytecode = { 0x03, 0x04, 0x05, 0x06 };
        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(bytecode);

        interpreter.execute();

        Stack<Integer> stack = interpreter.getOperandStack();
        assertEquals(4, stack.size(), "스택에 네 개의 요소가 있어야 한다");
        assertEquals(3, stack.pop(), "스택의 4번째 값은 3이어야 한다");
        assertEquals(2, stack.pop(), "스택의 3번째 값은 2이어야 한다");
        assertEquals(1, stack.pop(), "스택의 2번째 값은 1이어야 한다");
        assertEquals(0, stack.pop(), "스택의 1번째 값은 0이어야 한다");
    }

    @Test
    void testAddition() {
        byte[] bytecode = { 0x04, 0x05, 0x60 };
        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(bytecode);

        interpreter.execute();

        Stack<Integer> stack = interpreter.getOperandStack();
        assertEquals(1, stack.size(), "스택에 하나의 요소만 있어야 한다");
        assertEquals(3, stack.peek(), "스택의 최상위 요소는 3이어야 한다");
    }

    @Test
    void testThrowExceptionWhenAddition() {
        byte[] bytecode = { 0x04, 0x60 };
        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(bytecode);

        assertThrowsExactly(IllegalStateException.class, interpreter::execute);
    }

    @Test
    void testSubtraction() {
        byte[] bytecode = { 0x06, 0x04, 0x64 };
        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(bytecode);

        interpreter.execute();

        Stack<Integer> stack = interpreter.getOperandStack();
        assertEquals(1, stack.size(), "스택에 하나의 요소만 있어야 한다");
        assertEquals(2, stack.peek(), "스택의 최상위 요소는 2이어야 한다");
    }

    @Test
    void testThrowExceptionWhenSubtraction() {
        byte[] bytecode = { 0x04, 0x64 };
        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(bytecode);

        assertThrowsExactly(IllegalStateException.class, interpreter::execute);
    }

    @Test
    public void testLocalVariables() {
        byte[] bytecode = {0x05, 0x36, 0x01, 0x06, 0x36, 0x02, 0x15, 0x01, 0x15, 0x02, 0x60};
        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(bytecode);

        interpreter.execute();

        assertEquals(2, interpreter.getLocalVariable(1), "로컬 변수 1의 값은 2여야 합니다");
        assertEquals(3, interpreter.getLocalVariable(2), "로컬 변수 2의 값은 3이어야 합니다");

        Stack<Integer> stack = interpreter.getOperandStack();
        assertEquals(1, stack.size(), "스택에 하나의 요소만 있어야 합니다");
        assertEquals(5, stack.peek(), "스택의 최상위 값은 5여야 합니다 (2+3)");
    }

    @Test
    public void testThrowsWhenLocalVariables() {
        byte[] bytecode = {0x36, 0x03};
        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(bytecode);

        assertThrowsExactly(IllegalStateException.class, interpreter::execute);
    }
}
