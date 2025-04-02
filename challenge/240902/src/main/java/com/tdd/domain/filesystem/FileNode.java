package com.tdd.domain.filesystem;

public class FileNode implements FileSystemNode {
    private final int size;
    private final String name;

    public FileNode(String name, int size) {
        StringValidator.validate(name);

        this.name = name;
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public String getName() {
        return this.name;
    }
}
