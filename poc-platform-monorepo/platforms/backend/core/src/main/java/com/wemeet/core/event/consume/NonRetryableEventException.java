package com.wemeet.core.event.consume;

/** 재시도 무의미(역직렬화·validation 실패 등) → 즉시 DLT. */
public class NonRetryableEventException extends RuntimeException {
    public NonRetryableEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
