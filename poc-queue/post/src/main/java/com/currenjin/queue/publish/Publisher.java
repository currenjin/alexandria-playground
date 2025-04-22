package com.currenjin.queue.publish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Publisher {

    private final Logger logger = LoggerFactory.getLogger(Publisher.class);

    @Value("${post.queue.name}")
    private String queueName;

    private final RabbitTemplate template;

    public Publisher(RabbitTemplate template) {
        this.template = template;
    }

    public void send(Object message) {
        template.convertAndSend(queueName, message);

        logger.info("[우편 발송 요청 완료]");
    }

}