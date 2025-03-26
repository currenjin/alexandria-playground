package com.currenjin.jvm;

import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
