package com.currenjin.jvm;

import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SimpleJVMInterpreterTest {
    byte[] ICONST_0 = {0x03};

    @Test
    void testInterpreterCreation() {
        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(ICONST_0);

        assertNotNull(interpreter, "객체가 생성됩니다.");
    }

    @Test
    void testPushConstant() {
        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(ICONST_0);

        interpreter.execute();

        Stack<Integer> stack = interpreter.getOperandStack();
        assertEquals(1, stack.size(), "스택에 하나의 요소가 있어야 한다");
        assertEquals(0, stack.peek(), "스택의 최상위 값은 0이어야 한다");
    }
}
