package com.currenjin.jvm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SimpleJVMInterpreterTest {
    @Test
    void testInterpreterCreation() {
        byte[] bytecode = {0x03}; // ICONST_0
        SimpleJVMInterpreter interpreter = new SimpleJVMInterpreter(bytecode);
        assertNotNull(interpreter, "객체가 생성됩니다.");
    }
}
