package com.currenjin.music.share;

import java.time.LocalDateTime;

public interface DomainEvent {
	String getType();
	LocalDateTime getOccurredAt();
}
