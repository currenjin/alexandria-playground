package com.currenjin.song.service.publisher;

import com.currenjin.song.domain.event.DomainEvent;

public interface EventPublisher {
	void publish(DomainEvent event);
}
