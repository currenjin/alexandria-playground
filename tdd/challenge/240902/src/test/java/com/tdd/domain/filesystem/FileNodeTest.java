package com.tdd.domain.filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileNodeTest {
    @Test
    void file_node_size() {
        FileNode firstNode = new FileNode("first", 5);
        FileNode secondNode = new FileNode("first", 10);

        assertEquals(firstNode.getSize(), 5);
        assertEquals(secondNode.getSize(), 10);
    }

    @Test
    void file_node_name() {
        FileNode firstNode = new FileNode("first", 5);
        FileNode secondNode = new FileNode("second", 10);

        assertEquals(firstNode.getName(), "first");
        assertEquals(secondNode.getName(), "second");
    }

    @Test
    void name_cannot_be_null_or_empty_or_blank() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode(null, 5));
        assertThrows(IllegalArgumentException.class, () -> new FileNode("", 5));
        assertThrows(IllegalArgumentException.class, () -> new FileNode(" ", 5));
        assertThrows(IllegalArgumentException.class, () -> new FileNode("        ", 5));
    }
}
