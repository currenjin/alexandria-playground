package com.currenjin.markandsweep;

import com.currenjin.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MarkAndSweepTest {
    @Test
    void testGarbageCollectionRemovesUnreachableObjects() {
        List<ObjectNode> allObjects = new ArrayList<>();
        ObjectNode root = new ObjectNode();
        ObjectNode reachable = new ObjectNode();
        ObjectNode unreachable = new ObjectNode();

        root.addReference(reachable);

        allObjects.add(root);
        allObjects.add(reachable);
        allObjects.add(unreachable);

        assertEquals(3, allObjects.size());

        MarkAndSweep.run(allObjects, root);

        assertTrue(allObjects.contains(root));
        assertTrue(allObjects.contains(reachable));
        assertFalse(allObjects.contains(unreachable));
    }
}
