package com.currenjin.song.service.publisher;

import com.currenjin.song.share.DomainEvent;

public interface EventPublisher {
	void publish(DomainEvent event);
}
