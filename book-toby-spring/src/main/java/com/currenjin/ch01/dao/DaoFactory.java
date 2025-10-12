package com.currenjin.ch01.dao;

public class DaoFactory {
	public UserDao userDao() {
		ConnectionMaker connectionMaker = new DConnectionMaker();
		UserDao userDao = new DUserDao(connectionMaker);
		return userDao;
	}
}
