package com.currenjin.user.infrastructure;

import com.currenjin.user.service.publisher.EventPublisher;
import com.currenjin.user.share.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void publish(DomainEvent event) {
		try {
			String payload = objectMapper.writeValueAsString(event);
			kafkaTemplate.send(event.getType(), payload);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to serialize DomainEvent", e);
		}
	}
}
