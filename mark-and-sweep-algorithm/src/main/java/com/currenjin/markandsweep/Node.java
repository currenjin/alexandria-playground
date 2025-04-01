package com.currenjin.markandsweep;

import java.util.HashSet;
import java.util.Set;

public class Node {
    private final String id;
    private boolean marked;
    private final Set<Node> references;

    public Node(String id) {
        this.id = id;
        this.marked = false;
        this.references = new HashSet<>();
    }

    public String getId() {
        return this.id;
    }

    public boolean isMarked() {
        return this.marked;
    }

    public Set<Node> getReferences() {
        return this.references;
    }

    public void addReference(Node node) {
        this.references.add(node);
    }

    public int getReferenceSize() {
        return this.references.size();
    }

    public void removeReference(Node node) {
        this.references.remove(node);
    }

    public boolean containsReference(Node node) {
        return this.references.contains(node);
    }

    public void mark() {
        setMarked(true);
    }

    public void unmark() {
        setMarked(false);
    }

    private void setMarked(boolean marked) {
        this.marked = marked;
    }
}
