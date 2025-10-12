package com.currenjin.ch01.dao;

import java.sql.Connection;

public interface ConnectionMaker {
	public Connection makeNewConnection() throws ClassNotFoundException, java.sql.SQLException;
}
