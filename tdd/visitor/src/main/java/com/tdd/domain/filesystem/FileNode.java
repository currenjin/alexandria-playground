package com.tdd.domain.filesystem;

public class FileNode implements FileSystemNode {
    private final int size;
    private final String name;

    public FileNode(String name, int size) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        this.name = name;
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int accept(FileSystemVisitor visitor) {
        return visitor.visit(this);
    }
}
