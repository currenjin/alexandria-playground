package com.tdd.domain.filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileSizeVisitorTest {
    FileSizeVisitor visitor = new FileSizeVisitor();

    @Test
    void file_accept() {
        FileNode firstFile = new FileNode("file", 1);
        FileNode secondFile = new FileNode("file", 2);

        int firstSize = firstFile.accept(visitor);
        int secondSize = secondFile.accept(visitor);

        assertEquals(1, firstSize);
        assertEquals(2, secondSize);
    }

    @Test
    void directory_accept() {
        FileNode firstFile = new FileNode("file", 1);
        FileNode secondFile = new FileNode("file", 2);
        DirectoryNode directory = new DirectoryNode("directory");
        directory.addChildren(firstFile);
        directory.addChildren(secondFile);

        int totalSize = directory.accept(visitor);

        assertEquals(3, totalSize);
    }

    @Test
    void only_one_directory() {
        DirectoryNode directory = new DirectoryNode("directory");

        int totalSize = directory.accept(visitor);

        assertEquals(0, totalSize);
    }

    @Test
    void only_directory() {
        DirectoryNode firstDirectory = new DirectoryNode("directory");
        DirectoryNode secondDirectory = new DirectoryNode("directory");
        DirectoryNode integrationDirectory = new DirectoryNode("directory");
        integrationDirectory.addChildren(firstDirectory);
        integrationDirectory.addChildren(secondDirectory);

        int totalSize = integrationDirectory.accept(visitor);

        assertEquals(0, totalSize);
    }

    @Test
    void two_file_in_both_directory_in_directory() {
        FileNode firstFile = new FileNode("file", 1);
        FileNode secondFile = new FileNode("file", 2);
        DirectoryNode firstDirectory = new DirectoryNode("firstDirectory");
        DirectoryNode secondDirectory = new DirectoryNode("secondDirectory");
        DirectoryNode integrationDirectory = new DirectoryNode("integrationDirectory");

        firstDirectory.addChildren(firstFile);
        firstDirectory.addChildren(secondFile);
        int firstDirectorySize = firstDirectory.accept(visitor);
        assertEquals(3, firstDirectorySize);

        secondDirectory.addChildren(firstFile);
        secondDirectory.addChildren(secondFile);
        int secondDirectorySize = secondDirectory.accept(visitor);
        assertEquals(3, secondDirectorySize);

        integrationDirectory.addChildren(firstDirectory);
        integrationDirectory.addChildren(secondDirectory);
        int totalSize = integrationDirectory.accept(visitor);
        assertEquals(6, totalSize);
    }
}
