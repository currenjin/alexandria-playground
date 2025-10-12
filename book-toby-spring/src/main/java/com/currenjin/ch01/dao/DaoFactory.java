package com.currenjin.ch01.dao;

public class DaoFactory {
	public UserDao userDao() {
		UserDao userDao = new DUserDao(connectionMaker());
		return userDao;
	}

	public AccountDao accountDao() {
		AccountDao accountDao = new AccountDao(connectionMaker());
		return accountDao;
	}

	public MessageDao messageDao() {
		MessageDao messageDao = new MessageDao(connectionMaker());
		return messageDao;
	}

	private static ConnectionMaker connectionMaker() {
		ConnectionMaker connectionMaker = new DConnectionMaker();
		return connectionMaker;
	}
}
