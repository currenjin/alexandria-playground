package com.tdd.domain.filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DirectoryNodeTest {
    @Test
    void directory_name_test() {
        DirectoryNode firstDirectory = new DirectoryNode("first");
        DirectoryNode secondDirectory = new DirectoryNode("second");

        assertEquals("first", firstDirectory.getName());
        assertEquals("second", secondDirectory.getName());
    }

    @Test
    void directory_name_cannot_be_null_or_empty_blank() {
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(null));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(""));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(" "));
    }

    @Test
    void directory_has_children() {
        DirectoryNode node = new DirectoryNode("name");

        assertEquals(node.getChildren().size(), 0);
    }

    @Test
    void directory_add_child() {
        DirectoryNode directory = new DirectoryNode("name");

        FileSystemNode firstFile = new FileNode("first", 1);
        directory.add(firstFile);
        assertEquals(directory.getChildren().size(), 1);

        FileSystemNode secondFile = new FileNode("second", 1);
        directory.add(secondFile);
        assertEquals(directory.getChildren().size(), 2);

        FileSystemNode secondDirectory = new DirectoryNode("second");
        directory.add(secondDirectory);
        assertEquals(directory.getChildren().size(), 3);
    }
}
