package com.currenjin;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JdbcDriverTest {
	@Test
	@DisplayName("JDBC 드라이버 로딩 테스트")
	void driverLoadTest() {
		Enumeration<Driver> drivers = DriverManager.getDrivers();

		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			assertThat(driver.getClass().getName())
				.as("H2 드라이버가 로드되어 있어야 한다")
				.contains("h2");
		}
	}

	@Test
	@DisplayName("JDBC 드라이버 타입 확인")
	void driverTypeTest() throws Exception {
		String driverClassName = "org.h2.Driver";

		Class<?> driverClass = Class.forName(driverClassName);
		Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();

		assertThat(driver.jdbcCompliant())
			.as("JDBC 표준을 준수하는 Type 4 드라이버")
			.isTrue();
	}
}
