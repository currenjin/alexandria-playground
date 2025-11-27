package com.currenjin.user.service.publisher;

import com.currenjin.user.share.DomainEvent;

public interface EventPublisher {
	void publish(DomainEvent event);
}
