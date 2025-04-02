package com.tdd.domain.filesystem;

public class FileSizeVisitor implements FileSystemVisitor {
    @Override
    public int visit(FileNode node) {
        return node.getSize();
    }

    @Override
    public int visit(DirectoryNode directoryNode) {
        return directoryNode.getChildren()
                .stream()
                .mapToInt(child -> child.accept(this))
                .sum();
    }
}
