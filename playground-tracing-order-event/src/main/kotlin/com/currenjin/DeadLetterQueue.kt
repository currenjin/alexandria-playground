package com.currenjin

class DeadLetterQueue : AbstractMessageQueue() {
    override fun cleanupMessages() {}
}
