package com.currenjin.outboxinbox.core

interface InboxStore {
    fun exists(messageId: String): Boolean
    fun save(record: InboxRecord)
    fun markCompleted(messageId: String)
    fun markFailed(messageId: String, errorMessage: String)
    fun find(messageId: String): InboxRecord?
    fun findAll(): List<InboxRecord>
}
