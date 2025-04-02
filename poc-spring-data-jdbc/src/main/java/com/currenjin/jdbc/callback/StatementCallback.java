package com.currenjin.jdbc.callback;

import java.sql.SQLException;
import java.sql.Statement;

public interface StatementCallback<T> {
	T doInStatement(Statement statement) throws SQLException;
}
