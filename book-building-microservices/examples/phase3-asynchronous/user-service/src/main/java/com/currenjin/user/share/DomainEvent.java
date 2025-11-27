package com.currenjin.user.share;

import java.time.LocalDateTime;

public interface DomainEvent {
	String getType();
	LocalDateTime getOccurredAt();
}
