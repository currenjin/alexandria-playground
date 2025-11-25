package com.currenjin.song.infrastructure.publisher;

import com.currenjin.song.service.publisher.EventPublisher;
import com.currenjin.song.share.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public void publish(DomainEvent event) {
		kafkaTemplate.send(event.getType(), event);
	}
}
