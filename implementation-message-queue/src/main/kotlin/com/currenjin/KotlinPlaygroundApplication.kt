package com.currenjin

import com.currenjin.queues.QueueManager
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

class KotlinPlaygroundApplication

fun main() {
    println("===== 인메모리 메시지 큐 데모 =====")

    // 큐 관리자 생성
    val queueManager = QueueManager()

    // 기본 큐 생성 및 사용 테스트
    basicQueueTest(queueManager)

    // 생산자-소비자 패턴 테스트
    producerConsumerTest(queueManager)

    println("===== 데모 완료 =====")
}

/**
 * 기본 큐 작업 테스트
 */
fun basicQueueTest(queueManager: QueueManager) {
    println("\n----- 기본 큐 테스트 시작 -----")

    // 새 큐 생성
    val queue = queueManager.getOrCreateQueue("test-queue")
    println("큐 '${queue.name}' 생성됨")

    // 메시지 발행
    queue.publish("Hello, World!")
    queue.publish("This is a test message", mapOf("priority" to "high"))
    queue.publish("Another message")

    println("3개의 메시지가 발행됨, 현재 큐 크기: ${queue.size()}")

    // 메시지 소비
    println("\n소비된 메시지:")
    while (!queue.isEmpty()) {
        val message = queue.consume()
        println("- ${message?.payload} (ID: ${message?.id}, 헤더: ${message?.headers})")
    }

    println("큐가 비어 있음: ${queue.isEmpty()}")
    println("----- 기본 큐 테스트 완료 -----")
}

/**
 * 멀티스레드 환경에서 생산자-소비자 패턴 테스트
 */
fun producerConsumerTest(queueManager: QueueManager) {
    println("\n----- 생산자-소비자 테스트 시작 -----")

    val producerCount = 3
    val consumerCount = 2
    val messagesPerProducer = 5
    val totalMessages = producerCount * messagesPerProducer

    // 테스트에 사용할 새 큐 생성
    val queue = queueManager.getOrCreateQueue("producer-consumer-queue")
    println("큐 '${queue.name}' 생성됨")

    // 모든 스레드가 작업을 완료할 때까지 대기하기 위한 래치
    val latch = CountDownLatch(producerCount + consumerCount)

    // 소비된 메시지 수를 추적
    val consumedMessages = AtomicInteger(0)

    // 생산자 스레드 시작
    println("$producerCount 생산자 스레드 시작 중...")
    repeat(producerCount) { producerId ->
        thread {
            try {
                repeat(messagesPerProducer) { messageId ->
                    val payload = "Producer-$producerId Message-$messageId"
                    val headers = mapOf("producer" to producerId.toString())
                    queue.publish(payload, headers)
                    println("생산됨: $payload")
                    Thread.sleep((10..50).random().toLong()) // 랜덤 딜레이
                }
            } finally {
                latch.countDown()
            }
        }
    }

    // 소비자 스레드 시작
    println("$consumerCount 소비자 스레드 시작 중...")
    repeat(consumerCount) { consumerId ->
        thread {
            try {
                while (consumedMessages.get() < totalMessages) {
                    val message = queue.consume()
                    if (message != null) {
                        println("소비자-$consumerId 소비됨: ${message.payload}")
                        consumedMessages.incrementAndGet()
                    } else {
                        // 큐가 비어 있으면 잠시 대기
                        Thread.sleep(10)
                    }
                }
            } finally {
                latch.countDown()
            }
        }
    }

    // 모든 스레드가 완료될 때까지 대기
    latch.await()
    println("모든 메시지가 처리됨. 총 소비된 메시지: ${consumedMessages.get()}")
    println("----- 생산자-소비자 테스트 완료 -----")
}
