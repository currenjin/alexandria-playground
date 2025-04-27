package com.currenjin.queues

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class QueueManagerTest {
    private lateinit var queueManager: QueueManager

    @BeforeEach
    fun setup() {
        queueManager = QueueManager()
    }

    @Test
    fun `should start with no queues`() {
        assertTrue(queueManager.getQueueNames().isEmpty())
    }

    @Test
    fun `should create new queue`() {
        val queue = queueManager.getOrCreateQueue("test-queue")

        assertNotNull(queue)
        assertEquals("test-queue", queue.name)
        assertEquals(1, queueManager.getQueueNames().size)
        assertTrue(queueManager.getQueueNames().contains("test-queue"))
    }

    @Test
    fun `should return existing queue`() {
        val queue1 = queueManager.getOrCreateQueue("test-queue")

        val queue2 = queueManager.getOrCreateQueue("test-queue")

        assertSame(queue1, queue2)
        assertEquals(1, queueManager.getQueueNames().size)
    }

    @Test
    fun `should return null for non-existent queue`() {
        val queue = queueManager.getQueue("non-existent-queue")

        assertNull(queue)
    }

    @Test
    fun `should delete queue`() {
        queueManager.getOrCreateQueue("test-queue")

        val result = queueManager.deleteQueue("test-queue")

        assertTrue(result)
        assertTrue(queueManager.getQueueNames().isEmpty())
    }

    @Test
    fun `should return false when deleting non-existent queue`() {
        val result = queueManager.deleteQueue("non-existent-queue")

        assertFalse(result)
    }

    @Test
    fun `should manage multiple queues`() {
        queueManager.getOrCreateQueue("queue1")
        queueManager.getOrCreateQueue("queue2")
        queueManager.getOrCreateQueue("queue3")

        assertEquals(3, queueManager.getQueueNames().size)
        assertNotNull(queueManager.getQueue("queue1"))
        assertNotNull(queueManager.getQueue("queue2"))
        assertNotNull(queueManager.getQueue("queue3"))

        queueManager.deleteQueue("queue2")
        assertEquals(2, queueManager.getQueueNames().size)
        assertNull(queueManager.getQueue("queue2"))
    }
}
