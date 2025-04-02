package com.tdd.domain.user;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.arbitrary.MonkeyStringArbitrary;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {
    FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
            .register(User.class, arbitraryBuilder ->
                    arbitraryBuilder.giveMeBuilder(String.class)
                            .set("firstName", new MonkeyStringArbitrary().withCharRange('가', '힣'))
                            .set("lastName", new MonkeyStringArbitrary().withCharRange('가', '힣'))
            )
            .build();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository repository;

    private List<User> userList;
    private User firstUser;

    @BeforeEach
    void setUp() {
        userList = fixtureMonkey.giveMe(User.class, 5);
        firstUser = fixtureMonkey.giveMeOne(User.class);
    }

    @Test
    void creation() {
        Query query = em.createQuery("select count(u) from User u");
        Long before = (Long) query.getSingleResult();

        repository.saveAll(userList);

        assertEquals(before + 5L, query.getSingleResult());
    }

    @Test
    void creation_korean() {
        Query query = em.createQuery("select count(u) from User u");
        Long before = (Long) query.getSingleResult();

        repository.save(firstUser);

        assertEquals(before + 1L, query.getSingleResult());
    }
}