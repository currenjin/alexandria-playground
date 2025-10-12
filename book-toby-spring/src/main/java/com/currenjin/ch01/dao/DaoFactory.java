package com.currenjin.ch01.dao;

public class DaoFactory {
	public UserDao userDao() {
		ConnectionMaker connectionMaker = new DConnectionMaker();
		UserDao userDao = new DUserDao(connectionMaker);
		return userDao;
	}

	public AccountDao accountDao() {
		ConnectionMaker connectionMaker = new DConnectionMaker();
		AccountDao accountDao = new AccountDao(connectionMaker);
		return accountDao;
	}

	public MessageDao messageDao() {
		ConnectionMaker connectionMaker = new DConnectionMaker();
		MessageDao messageDao = new MessageDao(connectionMaker);
		return messageDao;
	}
}
