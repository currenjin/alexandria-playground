package com.currenjin.markandsweep;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {
    @Test
    void node_creation() {
        Node node = new Node("test");

        assertEquals("test", node.getId());
        assertFalse(node.isMarked());
        assertTrue(node.getReferences().isEmpty());
    }

    @Test
    void node_add_reference() {
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");

        nodeA.addReference(nodeB);

        assertTrue(nodeA.containsReference(nodeB));
        assertEquals(1, nodeA.getReferenceSize());

        assertFalse(nodeB.getReferences().contains(nodeA));
        assertEquals(0, nodeB.getReferenceSize());
    }

    @Test
    void node_remove_reference() {
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");

        nodeA.addReference(nodeB);
        nodeA.addReference(nodeC);

        assertTrue(nodeA.containsReference(nodeB));
        assertTrue(nodeA.containsReference(nodeC));
        assertEquals(2, nodeA.getReferenceSize());

        nodeA.removeReference(nodeB);

        assertFalse(nodeA.containsReference(nodeB));
        assertTrue(nodeA.containsReference(nodeC));
        assertEquals(1, nodeA.getReferenceSize());
    }

    @Test
    void node_mark() {
        Node node = new Node("A");
        assertFalse(node.isMarked());

        node.mark();
        assertTrue(node.isMarked());

        node.unmark();
        assertFalse(node.isMarked());
    }
}
