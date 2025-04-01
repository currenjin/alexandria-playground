package com.currenjin.markandsweep;

import com.currenjin.node.ObjectNode;

import java.util.List;

public class MarkAndSweep {
    public static void run(List<ObjectNode> allObjects, ObjectNode root) {
        for (ObjectNode node : allObjects) node.setMark(false);

        mark(root);

        allObjects.removeIf(node -> !node.getMarked());
    }

    private static void mark(ObjectNode node) {
        if (node == null || node.getMarked()) return;

        node.setMark(true);

        for (ObjectNode ref : node.getReferences()) mark(ref);
    }
}
