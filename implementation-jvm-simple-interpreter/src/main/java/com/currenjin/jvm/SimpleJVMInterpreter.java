package com.currenjin.jvm;

import java.util.Stack;

public class SimpleJVMInterpreter {
    private static final byte ICONST_0 = 0x03;
    private static final byte ICONST_1 = 0x04;
    private static final byte ICONST_2 = 0x05;
    private static final byte ICONST_3 = 0x06;

    private static final byte ILOAD = 0x15;
    private static final byte ISTORE = 0x36;

    private static final byte IADD = 0x60;
    private static final byte ISUB = 0x64;

    private final byte[] bytecode;
    private int pc = 0;
    private final Stack<Integer> operandStack = new Stack<>();
    private final int[] localVariables = new int[256];

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
                case ICONST_1:
                    operandStack.push(1);
                    break;
                case ICONST_2:
                    operandStack.push(2);
                    break;
                case ICONST_3:
                    operandStack.push(3);
                    break;
                case IADD: {
                    if (operandStack.size() < 2) {
                        throw new IllegalStateException("IADD requires at least two operands");
                    }

                    int value1 = operandStack.pop();
                    int value2 = operandStack.pop();

                    operandStack.push(value1 + value2);
                    break;
                }
                case ISUB: {
                    if (operandStack.size() < 2) {
                        throw new IllegalStateException("ISUB requires at least two operands");
                    }

                    int value1 = operandStack.pop();
                    int value2 = operandStack.pop();

                    operandStack.push(value2 - value1);
                    break;
                }
                case ISTORE: {
                    int index = bytecode[pc++] & 0xFF;
                    if (operandStack.isEmpty()) {
                        throw new IllegalStateException("ISTORE requires at least one operand");
                    }
                    localVariables[index] = operandStack.pop();
                    break;
                }
                case ILOAD: {
                    int index = bytecode[pc++] & 0xFF;
                    operandStack.push(localVariables[index]);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unsupported opcode: " + opcode);
            }
        }
    }

    public double getLocalVariable(int index) {
        return localVariables[index];
    }
}
