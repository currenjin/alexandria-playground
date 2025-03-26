package com.currenjin.jvm;

public class SimpleJVMInterpreter {
    private final byte[] bytecode;

    public SimpleJVMInterpreter(byte[] bytecode) {
        this.bytecode = bytecode;
    }
}
