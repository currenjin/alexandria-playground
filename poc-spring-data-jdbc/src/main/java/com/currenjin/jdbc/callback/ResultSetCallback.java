package com.currenjin.jdbc.callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetCallback<T> {
	T doInResultSet(ResultSet rs) throws SQLException;
}
