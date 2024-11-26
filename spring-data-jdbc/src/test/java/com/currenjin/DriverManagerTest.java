package com.currenjin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DriverManagerTest {
	private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
	private static final String USER = "sa";
	private static final String PASSWORD = "";

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

	@Test
	@DisplayName("드라이버 수동 로딩 테스트")
	void driverManualLoadTest() throws Exception {
		String driverName = "org.h2.Driver";

		Class<?> driverClass = Class.forName(driverName);
		Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
		DriverManager.registerDriver(driver);

		assertThat(DriverManager.getDrivers().hasMoreElements())
			.as("드라이버가 로딩되어야 함")
			.isTrue();

		assertThat(driver.getClass().getName())
			.as("H2 드라이버가 로딩되어야 함")
			.contains("h2");
	}

	@Test
	@DisplayName("Connection 획득 테스트 - URL")
	void getConnectionByUrlTest() throws SQLException {
		Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

		assertThat(connection).isNotNull();
		assertThat(connection.isClosed()).isFalse();

		connection.close();
	}

	@Test
	@DisplayName("Connection 획득 테스트 - Properties")
	void getConnectionByPropertiesTest() throws SQLException {
		Properties properties = new Properties();
		properties.setProperty("user", USER);
		properties.setProperty("password", PASSWORD);

		Connection connection = DriverManager.getConnection(URL, properties);

		assertThat(connection).isNotNull();
		assertThat(connection.isClosed()).isFalse();

		connection.close();
	}

	@Test
	@DisplayName("잘못된 URL로 Connection 획득 시도 테스트")
	void invalidUrlConnectionTest() {
		String invalidUrl = "test";

		assertThatThrownBy(() ->
			DriverManager.getConnection(invalidUrl, USER, PASSWORD))
			.isInstanceOf(SQLException.class);
	}

	@Test
	@DisplayName("Connection timeout 테스트")
	void connectionTimeoutTest() {
		DriverManager.setLoginTimeout(5);

		int timeout = DriverManager.getLoginTimeout();

		assertThat(timeout).isEqualTo(5);
	}

	@Test
	@DisplayName("Connection metadata 테스트")
	void connectionMeatadataTest() throws SQLException {
		Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

		DatabaseMetaData metadata = connection.getMetaData();

		assertThat(metadata.getDriverName()).contains("H2");
		assertThat(metadata.supportsTransactions()).isTrue();

		connection.close();
	}
}
