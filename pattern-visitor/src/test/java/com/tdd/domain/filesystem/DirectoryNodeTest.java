package com.tdd.domain.filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DirectoryNodeTest {
    @Test
    void directory_name() {
        DirectoryNode firstDirectory = new DirectoryNode("first_directory");
        DirectoryNode secondDirectory = new DirectoryNode("second_directory");

        assertEquals("first_directory", firstDirectory.getName());
        assertEquals("second_directory", secondDirectory.getName());
    }

    @Test
    void name_cannot_be_null() {
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(null));
    }

    @Test
    void name_cannot_be_empty() {
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(""));
    }

    @Test
    void directory_has_children() {
        DirectoryNode directory = new DirectoryNode("directory");

        assertEquals(0, directory.getChildren().size());
    }

    @Test
    void directory_add_children() {
        DirectoryNode directory = new DirectoryNode("directory");
        assertEquals(0, directory.getChildren().size());

        FileNode firstFile = new FileNode("first", 1);
        directory.addChildren(firstFile);
        assertEquals(1, directory.getChildren().size());

        FileNode secondFile = new FileNode("second", 1);
        directory.addChildren(secondFile);
        assertEquals(2, directory.getChildren().size());
    }

    @Test
    void directory_has_directory_has_directory() {
        DirectoryNode firstDirectory = new DirectoryNode("firstDirectory");
        assertEquals(0, firstDirectory.getChildren().size());

        DirectoryNode secondDirectory = new DirectoryNode("secondDirectory");
        secondDirectory.addChildren(firstDirectory);
        assertEquals(1, secondDirectory.getChildren().size());

        DirectoryNode thirdDirectory = new DirectoryNode("thirdDirectory");
        thirdDirectory.addChildren(secondDirectory);
        assertEquals(1, thirdDirectory.getChildren().size());
    }
}
