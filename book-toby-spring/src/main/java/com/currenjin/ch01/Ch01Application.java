import com.currenjin.ch01.dao.UserDao;
import com.currenjin.ch01.domain.User;

import java.sql.SQLException;

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
}
