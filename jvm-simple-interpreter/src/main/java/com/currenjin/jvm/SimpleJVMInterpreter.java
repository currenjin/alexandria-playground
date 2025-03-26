package com.currenjin.jvm;

import java.awt.*;
import java.util.Stack;

public class SimpleJVMInterpreter {
    private static final byte ICONST_0 = 0x03;

    private final byte[] bytecode;
    private int pc = 0;
    private Stack<Integer> operandStack = new Stack<>();

    public SimpleJVMInterpreter(byte[] bytecode) {
        this.bytecode = bytecode;
    }

    public Stack<Integer> getOperandStack() {
        Stack<Integer> copy = new Stack<>();
        copy.addAll(this.operandStack);
        return copy;
    }

    public void execute() {
        while (pc < bytecode.length) {
            byte opcode = bytecode[pc++];

            switch (opcode) {
                case ICONST_0:
                    operandStack.push(0);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported opcode: " + opcode);
            }
        }
    }
}
