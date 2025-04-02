package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DriverRepositoryTest {
	private final Driver driver = new Driver("name", "010-1234-1234");

	@Autowired
	private DriverRepository repository;

	@Test
	void save_test() {
		long beforeCount = repository.count();

		repository.save(driver);
		long afterCount = repository.count();

		assertEquals(beforeCount + 1, afterCount);
	}

	@Test
	void save_entity_value_test() {
		Driver actual = repository.save(driver);

		assertEquals(driver.getName(), actual.getName());
		assertEquals(driver.getPhoneNumber(), actual.getPhoneNumber());
	}

	@Test
	void find_by_id_test() {
		repository.save(driver);

		Driver actual = repository.findById(driver.getId())
			.orElseThrow(() -> new IllegalArgumentException("Driver not found"));

		assertEquals(driver.getName(), actual.getName());
		assertEquals(driver.getPhoneNumber(), actual.getPhoneNumber());
	}
}
