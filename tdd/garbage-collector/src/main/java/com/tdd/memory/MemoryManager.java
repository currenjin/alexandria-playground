package com.tdd.memory;

import javax.swing.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MemoryManager {
    private Set<Object> references = new HashSet<>();

    public Object allocate() {
        return new Object();
    }

    public void addReference(Object obj) {
        references.add(obj);
    }

    public boolean isReferenced(Object obj) {
        return references.contains(obj);
    }

    public void removeReference(Object obj) {
        references.remove(obj);
    }

    public void collectGarbage() {
        references.removeIf(obj -> !isReferenced(obj));
    }
}
