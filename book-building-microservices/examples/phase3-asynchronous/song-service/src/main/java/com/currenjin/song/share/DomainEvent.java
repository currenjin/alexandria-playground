package com.currenjin.song.share;

import java.time.LocalDateTime;

public interface DomainEvent {
	String getType();
	LocalDateTime getOccurredAt();
}
