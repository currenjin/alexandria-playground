package com.currenjin.song.infrastructure.publisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.currenjin.song.domain.event.DomainEvent;
import com.currenjin.song.service.publisher.EventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public void publish(DomainEvent event) {
		kafkaTemplate.send(event.getType(), event);
	}
}
