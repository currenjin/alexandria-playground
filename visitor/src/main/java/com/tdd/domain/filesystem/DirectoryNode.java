package com.tdd.domain.filesystem;

import java.util.ArrayList;
import java.util.List;

public class DirectoryNode implements FileSystemNode {
    private final String name;
    private final List<FileSystemNode> children = new ArrayList<>();

    public DirectoryNode(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public List<FileSystemNode> getChildren() {
        return this.children;
    }

    public void addChildren(FileSystemNode file) {
        this.children.add(file);
    }

    @Override
    public int accept(FileSystemVisitor visitor) {
        return visitor.visit(this);
    }
}
