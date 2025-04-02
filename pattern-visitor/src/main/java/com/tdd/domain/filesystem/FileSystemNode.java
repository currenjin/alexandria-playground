package com.tdd.domain.filesystem;

public interface FileSystemNode {
    int accept(FileSystemVisitor visitor);
}
