package com.tdd.memory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryManagerTest {
    @Test
    void testMemoryAllocation() {
        MemoryManager memoryManager = new MemoryManager();
        Object obj = memoryManager.allocate();
        assertNotNull(obj);
    }

    @Test
    void testReferenceTracking() {
        MemoryManager memoryManager = new MemoryManager();

        Object obj1 = memoryManager.allocate();
        Object obj2 = memoryManager.allocate();

        memoryManager.addReference(obj1);
        memoryManager.addReference(obj2);

        assertTrue(memoryManager.isReferenced(obj1));
        assertTrue(memoryManager.isReferenced(obj2));
    }

    @Test
    void testGarbageCollection() {
        MemoryManager memoryManager = new MemoryManager();

        Object obj1 = memoryManager.allocate();
        Object obj2 = memoryManager.allocate();

        memoryManager.addReference(obj1);
        memoryManager.addReference(obj2);

        memoryManager.removeReference(obj1);

        memoryManager.collectGarbage();

        assertFalse(memoryManager.isReferenced(obj1));
        assertTrue(memoryManager.isReferenced(obj2));
    }
}
