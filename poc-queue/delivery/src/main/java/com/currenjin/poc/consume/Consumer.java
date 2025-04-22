package com.currenjin.poc.consume;

import com.currenjin.poc.publish.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private final Publisher publisher;

    public Consumer(Publisher publisher) {
        this.publisher = publisher;
    }

    @RabbitListener(queues = "${post.queue.name}")
    public void receiveMessage(String message) {
        logger.info("[배송 요청 발생]");
        logger.info("내용 : " + message);

        publisher.send(message);
    }
}
