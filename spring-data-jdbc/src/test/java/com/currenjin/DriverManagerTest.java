package com.currenjin;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DriverManagerTest {
	@Test
	@DisplayName("드라이버 자동 로딩 테스트")
	void driverAutoLoadingTest() {
		Enumeration<Driver> drivers = DriverManager.getDrivers();

		assertThat(drivers.hasMoreElements())
			.as("드라이버가 자동으로 로딩되어야 함")
			.isTrue();

		Driver driver = drivers.nextElement();
		assertThat(driver.getClass().getName())
			.as("H2 드라이버가 로딩되어야 함")
			.contains("h2");
	}
}
