package com.currenjin.poc.publish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Publisher {

    private final Logger logger = LoggerFactory.getLogger(Publisher.class);

    @Value("${delivery.queue.name}")
    private String queueName;

    private final RabbitTemplate rabbitTemplate;

    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Object message) {
        rabbitTemplate.convertAndSend(queueName, message);

        logger.info("[배송 완료]");
    }

}