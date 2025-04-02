package com.trevari.zipkinpoc.publish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class Publisher {

    private final Logger logger = LoggerFactory.getLogger(Publisher.class);

    @Value("${delivery.queue.name}")
    private String queueName;

    private final QueueMessagingTemplate messagingTemplate;

    public Publisher(QueueMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void send(Object message) {
        messagingTemplate.convertAndSend(queueName, message);

        logger.info("[배송 완료]");
    }

}