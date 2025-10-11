package com.currenjin.ch01;

import java.sql.SQLException;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.currenjin.ch01.dao.UserDao;
import com.currenjin.ch01.domain.User;

@SpringBootApplication
public class Ch01Application {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		UserDao userDao = new UserDao();

		User user = new User();
		user.setId("currenjin");
		user.setName("정현진");
		user.setPassword("123456");

		userDao.add(user);
		System.out.println(user.getId() + " 등록 성공");

		User user2 = userDao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		System.out.println(user2.getId() + " 조회 성공");
	}
}
