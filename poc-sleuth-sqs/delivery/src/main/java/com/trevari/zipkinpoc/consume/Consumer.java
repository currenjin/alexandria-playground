package com.trevari.zipkinpoc.consume;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.trevari.zipkinpoc.publish.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private final Publisher publisher;

    public Consumer(Publisher publisher) {
        this.publisher = publisher;
    }

    @SqsListener("${post.queue.name}")
    public void receiveMessage(String message, @Headers Map<String, MessageAttributeValue> sqsHeaders) {
        logger.info("[배송 요청 발생]");
        logger.info("내용 : " + message);

        publisher.send(message);
    }
}
