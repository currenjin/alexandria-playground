package com.tdd.domain.filesystem;

import java.util.ArrayList;
import java.util.List;

public class DirectoryNode implements FileSystemNode {
    private final String name;
    private final List<FileSystemNode> children = new ArrayList<>();

    public DirectoryNode(String name) {
        StringValidator.validate(name);

        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public List<FileSystemNode> getChildren() {
        return this.children;
    }

    public void add(FileSystemNode node) {
        this.children.add(node);
    }
}
