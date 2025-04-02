package com.tdd.domain.filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileNodeTest {
    @Test
    void file_node_size() {
        FileNode first_node = new FileNode("first_file", 5);
        FileNode second_node = new FileNode("second_file", 10);

        assertEquals(first_node.getSize(), 5);
        assertEquals(second_node.getSize(), 10);
    }

    @Test
    void file_node_name() {
        FileNode first_node = new FileNode("first_file", 5);
        FileNode second_node = new FileNode("second_file", 10);

        assertEquals(first_node.getName(), "first_file");
        assertEquals(second_node.getName(), "second_file");
    }

    @Test
    void name_cannot_be_null() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode(null, 5));
    }

    @Test
    void name_cannot_be_empty() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("", 5));
    }

    @Test
    void size_cannot_be_negative() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("", -1));
    }
}
