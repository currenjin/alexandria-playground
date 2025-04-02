package com.currenjin.markandsweep;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

public class ObjectGraphTest {

    private ObjectGraph graph;

    @BeforeEach
    public void setUp() {
        graph = new ObjectGraph();
    }

    @Test
    public void testAddNode() {
        Node nodeA = new Node("A");

        graph.addNode(nodeA);

        assertTrue(graph.getNodes().contains(nodeA));
        assertEquals(1, graph.getNodes().size());
    }

    @Test
    public void testSetRoot() {
        Node root = new Node("root");
        graph.addNode(root);

        graph.setRoot(root);

        assertEquals(root, graph.getRoot());
    }

    @Test
    public void testFindReachableNodes() {
        Node root = new Node("root");
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node isolatedNode = new Node("isolated");

        graph.addNode(root);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(isolatedNode);

        root.addReference(nodeA);
        nodeA.addReference(nodeB);
        nodeB.addReference(nodeC);

        graph.setRoot(root);

        Set<Node> reachableNodes = graph.findReachableNodes();

        assertTrue(reachableNodes.contains(root));
        assertTrue(reachableNodes.contains(nodeA));
        assertTrue(reachableNodes.contains(nodeB));
        assertTrue(reachableNodes.contains(nodeC));
        assertFalse(reachableNodes.contains(isolatedNode));
        assertEquals(4, reachableNodes.size());
    }

    @Test
    public void testFindReachableNodesWithCircularReferences() {
        Node root = new Node("root");
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");

        graph.addNode(root);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);

        root.addReference(nodeA);
        nodeA.addReference(nodeB);
        nodeB.addReference(nodeC);
        nodeC.addReference(nodeA);

        graph.setRoot(root);

        Set<Node> reachableNodes = graph.findReachableNodes();

        assertTrue(reachableNodes.contains(root));
        assertTrue(reachableNodes.contains(nodeA));
        assertTrue(reachableNodes.contains(nodeB));
        assertTrue(reachableNodes.contains(nodeC));
        assertEquals(4, reachableNodes.size());
    }
}