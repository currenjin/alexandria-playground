package com.currenjin.node;

import java.util.ArrayList;
import java.util.List;

public class ObjectNode {
    private boolean marked = false;
    private List<ObjectNode> references = new ArrayList<>();

    public void addReference(ObjectNode reachable) {
        references.add(reachable);
    }

    public void setMark(boolean marked) {
        this.marked = marked;
    }

    public boolean getMarked() {
        return this.marked;
    }

    public List<ObjectNode> getReferences() {
        return this.references;
    }
}
