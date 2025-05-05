package com.currenjin.messages

import java.time.LocalDateTime
import java.util.UUID

/**
 * 메시지 큐에서 전송되는 메시지
 *
 * @property id 고유 식별자
 * @property payload 메시지 내용
 * @property timestamp 메시지 생성 시각
 * @property headers 메시지에 관련된 메타데이터
 *
 * @author currenjin
 */
data class Message(
    val id: String = UUID.randomUUID().toString(),
    val payload: String,
    val timestamp: String = LocalDateTime.now().toString(),
    val headers: Map<String, String> = mapOf(),
) {
    fun toLogString(): String = "$timestamp::$id::$payload"
}
