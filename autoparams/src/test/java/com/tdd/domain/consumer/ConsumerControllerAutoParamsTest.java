package com.tdd.domain.consumer;

import autoparams.AutoSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("POST /api/consumer/signup")
public class ConsumerControllerAutoParamsTest {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ConsumerJpaRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @ParameterizedTest
    @AutoSource
    void 올바른_정보를_사용해_요청하면_성공_상태코드를_반환한다(Signup signup) {
        String path = "/api/consumer/signup";

        ResponseEntity<Void> response = client.postForEntity(path, signup, Void.class);

        assertEquals(204, response.getStatusCodeValue());
    }

    @ParameterizedTest
    @AutoSource
    void 존재하는_이메일_주소로_요청하면_400_코드를_반환한다(Signup signup, String otherPassword) {
        String path = "/api/consumer/signup";
        client.postForEntity(path, signup, Void.class);

        ResponseEntity<Void> response = client.postForEntity(
                path,
                new Signup(signup.getEmail(), otherPassword),
                Void.class
        );

        assertEquals(400, response.getStatusCodeValue());
    }
}