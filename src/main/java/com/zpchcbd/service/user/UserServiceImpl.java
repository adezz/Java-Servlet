package com.zpchcbd.service.user;

import com.zpchcbd.dao.BaseDao;
import com.zpchcbd.dao.user.UserDao;
import com.zpchcbd.dao.user.UserDaoIml;
import com.zpchcbd.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;


public class UserServiceImpl implements UserService {
    UserDao userDao = null;

    public UserServiceImpl(){
        userDao = new UserDaoIml();
    }

    public User login(String username, String password) throws SQLException, ClassNotFoundException {
        User user = null;
        user = userDao.getLoginUser(username);
        if(user.getUserPassword().equals(password)){
            return user;
        }
        return null;
    }

    public boolean updatePwd(String username, String password) throws SQLException, ClassNotFoundException {
        int flag = 0;
        flag = userDao.updatePwd(username, password);
        if (flag == 1)
            return true;
        else
            return false;
    }

    public int getUserSize(String username, int userRole) throws SQLException, ClassNotFoundException {
        int Size = userDao.getUserSize(username, userRole);
        return Size;
    }

    @Test
    
    public void test01(){

    }
}
