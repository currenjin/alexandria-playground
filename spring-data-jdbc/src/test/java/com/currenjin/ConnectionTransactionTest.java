package com.currenjin;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConnectionTransactionTest {
	Connection connection;

	@AfterEach
	void tearDown() throws SQLException {
		connection.close();
	}

	@Test
	@DisplayName("트랜잭션 커밋 테스트")
	void transactionCommitTest() throws SQLException {
		connection = ConnectionManager.getConnection();
		connection.setAutoCommit(false);
		Statement stmt = connection.createStatement();

		stmt.executeUpdate("CREATE TABLE test (id INT PRIMARY KEY)");
		stmt.executeUpdate("INSERT INTO test VALUES (1)");
		connection.commit();

		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM test");
		rs.next();
		assertThat(rs.getInt(1)).isEqualTo(1);
	}

	@Test
	@DisplayName("트랜잭션 롤백 테스트")
	void transactionRollbackTest() throws SQLException {
		connection = ConnectionManager.getConnection();
		connection.setAutoCommit(false);
		Statement stmt = connection.createStatement();

		stmt.executeUpdate("CREATE TABLE test2 (id INT PRIMARY KEY)");
		stmt.executeUpdate("INSERT INTO test2 VALUES (1)");
		connection.rollback();

		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM test2");
		rs.next();
		assertThat(rs.getInt(1)).isEqualTo(0);
	}

	@Test
	@DisplayName("AutoCommit 설정 테스트")
	void autoCommitTest() throws SQLException {
		connection = ConnectionManager.getConnection();

		assertThat(connection.getAutoCommit()).isTrue();

		connection.setAutoCommit(false);
		assertThat(connection.getAutoCommit()).isFalse();

		connection.setAutoCommit(true);
		assertThat(connection.getAutoCommit()).isTrue();
	}
}
