package com.currenjin.queues

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * 여러 인메모리 큐를 관리하는 클래스
 *
 * 이 클래스는 이름으로 여러 InMemoryQueue 인스턴스를 생성, 검색 및 삭제하는 기능을 제공
 *
 * @author currenjin
 */
class QueueManager {
    private val queues = mutableMapOf<String, InMemoryQueue>()

    private val lock = ReentrantLock()

    /**
     * 지정된 이름으로 큐를 가져오거나 존재하지 않는 경우 새 큐를 생성
     *
     * @param queueName 큐 이름
     * @return 검색되거나 새로 생성된 InMemoryQueue
     */
    fun getOrCreateQueue(queueName: String): InMemoryQueue =
        lock.withLock {
            queues.getOrPut(queueName) { InMemoryQueue(queueName) }
        }

    /**
     * 지정된 이름으로 존재하는 큐를 가져옵니다.
     * 큐가 존재하지 않으면 null을 반환
     *
     * @param queueName 큐 이름
     * @return InMemoryQueue 또는 존재하지 않는 경우 null
     */
    fun getQueue(queueName: String): InMemoryQueue? =
        lock.withLock {
            queues[queueName]
        }

    /**
     * 관리되는 모든 큐의 이름 세트를 반환
     *
     * @return 큐 이름 세트
     */
    fun getQueueNames(): Set<String> =
        lock.withLock {
            queues.keys.toSet()
        }

    /**
     * 지정된 이름의 큐를 삭제
     *
     * @param queueName 삭제할 큐의 이름
     * @return 큐가 존재하고 성공적으로 삭제된 경우 true, 존재하지 않는 경우 false
     */
    fun deleteQueue(queueName: String): Boolean =
        lock.withLock {
            queues.remove(queueName) != null
        }

    /**
     * 모든 큐를 삭제
     */
    fun deleteAllQueues() =
        lock.withLock {
            queues.clear()
        }

    /**
     * 모든 큐 인스턴스의 통계를 반환
     *
     * @return 큐 이름과 현재 메시지 수를 포함한 맵
     */
    fun getQueueStatistics(): Map<String, Int> =
        lock.withLock {
            queues.mapValues { (_, queue) -> queue.size() }
        }
}
