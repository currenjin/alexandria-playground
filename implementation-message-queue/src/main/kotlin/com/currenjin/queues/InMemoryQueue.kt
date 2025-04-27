package com.currenjin.queues

import com.currenjin.messages.Message
import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * 이 클래스는 선입선출(FIFO) 방식으로 메시지를 저장하고 검색하는 기본 메시지 큐
 * 메시지는 메모리에만 저장되며, 애플리케이션이 종료되면 모든 메시지가 손실됨
 *
 * @property name 큐의 이름
 *
 * @author currenjin
 */
class InMemoryQueue(
    val name: String,
) {
    private val messageQueue: Queue<Message> = LinkedList()

    private val lock = ReentrantLock()

    /**
     * 메시지를 큐에 발행
     *
     * @param payload 메시지 내용
     * @param headers 메시지와 관련된 선택적 메타데이터
     * @return 발행된 메시지
     */
    fun publish(
        payload: String,
        headers: Map<String, String> = mapOf(),
    ): Message {
        val message = Message(payload = payload, headers = headers)
        lock.withLock {
            messageQueue.offer(message)
        }
        return message
    }

    /**
     * 큐에서 다음 메시지를 소비
     * 큐가 비어 있으면 null을 반환
     *
     * @return 다음 메시지 또는 큐가 비어 있으면 null
     */
    fun consume(): Message? =
        lock.withLock {
            messageQueue.poll()
        }

    /**
     * 큐에 있는 메시지 수를 반환
     *
     * @return 큐의 현재 크기
     */
    fun size(): Int =
        lock.withLock {
            messageQueue.size
        }

    /**
     * 큐가 비어 있는지 여부를 반환
     *
     * @return 큐가 비어 있으면 true, 그렇지 않으면 false
     */
    fun isEmpty(): Boolean =
        lock.withLock {
            messageQueue.isEmpty()
        }

    /**
     * 큐의 다음 메시지를 제거하지 않고 조회
     * 큐가 비어 있으면 null을 반환
     *
     * @return 다음 메시지 또는 큐가 비어 있으면 null
     */
    fun peek(): Message? =
        lock.withLock {
            messageQueue.peek()
        }

    /**
     * 큐의 모든 메시지를 제거
     */
    fun clear() =
        lock.withLock {
            messageQueue.clear()
        }
}
