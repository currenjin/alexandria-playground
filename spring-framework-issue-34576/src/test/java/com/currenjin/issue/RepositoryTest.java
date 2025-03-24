package com.currenjin.issue;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import com.currenjin.person.Person;
import com.currenjin.person.PersonRepository;

@DataJpaTest
public class RepositoryTest {

	@Autowired
	private PersonRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@BeforeEach
	void setUp() {
		System.out.println("Setting up test data in outer class");
		entityManager.persistAndFlush(new Person());
	}

	@Test
	void shouldFindAllPersons() {
		System.out.println("Running test in outer class");

		Iterable<Person> result = repository.findAll();

		assertThat(result).hasSizeGreaterThanOrEqualTo(1);
	}

	@TestPropertySource(properties = "spring.config.additional-location=optional:file:data.yaml")
	@Nested
	class TransactionalRequireExceptionTest {

		@Test
		void shouldFindAllPersons() {
			System.out.println("Running test in nested class");

			Iterable<Person> result = repository.findAll();

			assertThat(result).hasSizeGreaterThanOrEqualTo(1);
		}
	}
}
