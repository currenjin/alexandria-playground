package com.currenjin;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StatementTest {
	private Connection connection;

	@BeforeEach
	void setup() throws SQLException {
		connection = ConnectionManager.getConnection();

		try (Statement stmt = connection.createStatement()) {
			stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(50))");
		}
	}

	@AfterEach
	void cleanup() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			try (Statement stmt = connection.createStatement()) {
				stmt.execute("DROP TABLE IF EXISTS users");
			}
			ConnectionManager.closeConnection(connection);
		}
	}

	@Test
	@DisplayName("SQL Injection 취약성 테스트")
	void sqlInjectionTest() throws SQLException {
		String maliciousName = "' OR '1'='1";

		Statement stmt = connection.createStatement();
		stmt.executeUpdate("INSERT INTO users VALUES (1, 'validUser')");
		ResultSet rs1 = stmt.executeQuery(
			"SELECT * FROM users WHERE name = '" + maliciousName + "'"
		);

		assertThat(rs1.next()).isTrue();

		PreparedStatement pstmt = connection.prepareStatement(
			"SELECT * FROM users WHERE name = ?"
		);
		pstmt.setString(1, maliciousName);
		ResultSet rs2 = pstmt.executeQuery();

		assertThat(rs2.next()).isFalse();
	}

	@Test
	@DisplayName("Statement vs PreparedStatement 성능 비교")
	void performanceComparisonTest() throws SQLException {
		int iterations = 1000;

		long statementStart = System.nanoTime();
		Statement stmt = connection.createStatement();
		for (int i = 0; i < iterations; i++) {
			stmt.executeUpdate(
				"INSERT INTO users VALUES (" + i + ", 'user" + i + "')"
			);
		}
		long statementTime = System.nanoTime() - statementStart;

		stmt.executeUpdate("DELETE FROM users");

		long preparedStart = System.nanoTime();
		PreparedStatement pstmt = connection.prepareStatement(
			"INSERT INTO users VALUES (?, ?)"
		);
		for (int i = 0; i < iterations; i++) {
			pstmt.setInt(1, i);
			pstmt.setString(2, "user" + i);
			pstmt.executeUpdate();
		}
		long preparedTime = System.nanoTime() - preparedStart;

		System.out.println("Statement Time: " + statementTime / 1_000_000 + "ms");
		System.out.println("PreparedStatement Time: " + preparedTime / 1_000_000 + "ms");

		assertThat(preparedTime).isLessThan(statementTime);
	}

	@Test
	@DisplayName("배치 처리 성능 테스트")
	void batchPerformanceTest() throws SQLException {
		int batchSize = 1000;

		connection.setAutoCommit(false);
		PreparedStatement pstmt = connection.prepareStatement(
			"INSERT INTO users VALUES (?, ?)"
		);

		long batchStart = System.nanoTime();
		for (int i = 0; i < batchSize; i++) {
			pstmt.setInt(1, i);
			pstmt.setString(2, "user" + i);
			pstmt.addBatch();

			if (i % 100 == 0) {
				pstmt.executeBatch();
			}
		}
		pstmt.executeBatch();
		connection.commit();
		long batchTime = System.nanoTime() - batchStart;

		Statement queryStmt = connection.createStatement();
		ResultSet rs = queryStmt.executeQuery("SELECT COUNT(*) FROM users");
		rs.next();
		assertThat(rs.getInt(1)).isEqualTo(batchSize);
		System.out.println("Batch Insert Time: " + batchTime / 1_000_000 + "ms");
	}

	@Test
	@DisplayName("PreparedStatement 재사용 테스트")
	void preparedStatementReuseTest() throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement(
			"INSERT INTO users VALUES (?, ?)"
		);

		pstmt.setInt(1, 1);
		pstmt.setString(2, "user1");
		int firstUpdate = pstmt.executeUpdate();

		pstmt.setInt(1, 2);
		pstmt.setString(2, "user2");
		int secondUpdate = pstmt.executeUpdate();

		assertThat(firstUpdate).isEqualTo(1);
		assertThat(secondUpdate).isEqualTo(1);

		ResultSet rs = connection.createStatement()
			.executeQuery("SELECT COUNT(*) FROM users");
		rs.next();
		assertThat(rs.getInt(1)).isEqualTo(2);
	}
}
