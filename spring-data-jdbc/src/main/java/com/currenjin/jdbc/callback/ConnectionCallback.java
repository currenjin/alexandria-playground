package com.currenjin.jdbc.callback;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionCallback<T> {
	T doInConnection(Connection connection) throws SQLException;
}
