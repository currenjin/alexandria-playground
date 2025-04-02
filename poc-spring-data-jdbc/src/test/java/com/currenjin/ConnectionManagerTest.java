package com.currenjin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConnectionManagerTest {
	private Connection connection;

	@AfterEach
	void tearDown() throws SQLException {
		connection.close();
	}

	@Test
	@DisplayName("Connection 획득 테스트")
	void getConnectionTest() throws SQLException {
		connection = ConnectionManager.getConnection();

		assertThat(connection).isNotNull();
		assertThat(connection.isClosed()).isFalse();

		ConnectionManager.closeConnection(connection);
	}

	@Test
	@DisplayName("Close Connection 테스트")
	void closeConnectionTest() throws SQLException {
		connection = ConnectionManager.getConnection();
		assertFalse(connection.isClosed());

		ConnectionManager.closeConnection(connection);

		assertTrue(connection.isClosed());
	}

	@Test
	@DisplayName("이미 closed 된 connection close 시도 테스트")
	void closeClosedConnectionTest() throws SQLException {
		connection = ConnectionManager.getConnection();
		ConnectionManager.closeConnection(connection);
		assertTrue(connection.isClosed());

		ConnectionManager.closeConnection(connection);
		assertTrue(connection.isClosed());
	}

	@Test
	@DisplayName("Connection timeout 테스트")
	void connectionTimeoutTest() throws SQLException {
		connection = ConnectionManager.getConnection(5);

		int timeout = DriverManager.getLoginTimeout();

		assertThat(timeout).isEqualTo(5);
	}

	@Test
	@DisplayName("Connection rollback 테스트")
	void rollbackTest() throws SQLException {
		connection = ConnectionManager.getConnection();
		connection.setAutoCommit(false);

		var statement = connection.createStatement();
		statement.executeUpdate("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255))");
		statement.executeUpdate("INSERT INTO test_table VALUES (1, 'test')");

		ConnectionManager.rollback(connection);

		ResultSet rs = statement.executeQuery("SELECT * FROM test_table");
		assertFalse(rs.next());
	}
}