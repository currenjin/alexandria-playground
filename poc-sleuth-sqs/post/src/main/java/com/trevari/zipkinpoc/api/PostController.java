package com.trevari.zipkinpoc.api;

import com.trevari.zipkinpoc.publish.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final Publisher publisher;

    public PostController(Publisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping
    public void sendMessage(@RequestBody String message) {
        logger.info("[우편 발송 요청]");
        logger.info("내용 : " + message);

        publisher.send(message);
    }
}