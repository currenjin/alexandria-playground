package com.currenjin.song.domain.event;

import java.time.LocalDateTime;

public interface DomainEvent {
	String getTopic();
	String getType();
	LocalDateTime getOccurredAt();
}
