package com.tdd.domain.consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("POST /api/consumer/signup")
public class ConsumerControllerTest {
    String path = "/api/consumer/signup";

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ConsumerJpaRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void 올바른_정보를_사용해_요청하면_성공_상태코드를_반환한다() {
        Signup command = new Signup("user@test.com", "my password");

        ResponseEntity<Void> response = client.postForEntity(path, command, Void.class);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void 존재하는_이메일_주소로_요청하면_400_코드를_반환한다() {
        String duplicatedMail = "user@test.com";
        Signup command1 = new Signup(duplicatedMail, "my password");
        client.postForEntity(path, command1, Void.class);

        Signup command2 = new Signup(duplicatedMail, "my password");
        ResponseEntity<Void> response = client.postForEntity(path, command2, Void.class);

        assertEquals(400, response.getStatusCodeValue());
    }
}