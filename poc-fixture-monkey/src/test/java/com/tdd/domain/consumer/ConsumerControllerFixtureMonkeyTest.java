package com.tdd.domain.consumer;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
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
public class ConsumerControllerFixtureMonkeyTest {
    String path = "/api/consumer/signup";
    FixtureMonkey fixtureMonkey = FixtureMonkey.builder().objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE).build();

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
        Signup signup = fixtureMonkey.giveMeOne(Signup.class);

        ResponseEntity<Void> response = client.postForEntity(path, signup, Void.class);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void 존재하는_이메일_주소로_요청하면_400_코드를_반환한다() {
        Signup signup1 = fixtureMonkey.giveMeBuilder(Signup.class)
                .set("email", "test@email.com")
                .sample();
        Signup signup2 = fixtureMonkey.giveMeBuilder(Signup.class)
                .set("email", "test@email.com")
                .sample();
        client.postForEntity(path, signup1, Void.class);

        ResponseEntity<Void> response = client.postForEntity(path, signup2, Void.class);

        assertEquals(400, response.getStatusCodeValue());
    }
}