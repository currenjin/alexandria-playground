package com.currenjin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
	private static final String USER = "sa";
	private static final String PASSWORD = "";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public static Connection getConnection(int loginTimeout) throws SQLException {
		DriverManager.setLoginTimeout(loginTimeout);
		return getConnection();
	}

	public static void closeConnection(Connection connection) throws SQLException {
		connection.close();
	}

	public static void rollback(Connection connection) throws SQLException {
		connection.rollback();
	}
}
