package com.zpchcbd.service.user;

import com.zpchcbd.dao.BaseDao;
import com.zpchcbd.dao.user.UserDao;
import com.zpchcbd.dao.user.UserDaoIml;
import com.zpchcbd.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserServiceImpl implements UserService {
    private UserDao userDao = null;

    public UserServiceImpl() {
        userDao = new UserDaoIml();
    }

    public User login(String username, String password) throws SQLException, ClassNotFoundException {
        Connection connection = BaseDao.getConnection();
        User user = null;
        if (connection != null){
            user = userDao.getLoginUser(connection, username);
            if (user.getUserPassword().equals(password)) {
                return user;
            }
        }
        connection.close();
        return null;
    }

    public boolean updatePwd(String username, String password) throws SQLException, ClassNotFoundException {
        Connection connection = BaseDao.getConnection();
        int flag = 0;
        flag = userDao.updatePwd(connection, username, password);
        connection.close();

        if (flag == 1)
            return true;
        else
            return false;
    }

    public int getUserSize(String userName, int userRole) throws SQLException, ClassNotFoundException {
        Connection connection = BaseDao.getConnection();
        int Size = userDao.getUserSize(connection, userName, userRole);
        connection.close();

        return Size;
    }

    public List<User> getUserList(String userName, int userRole, int pageSize, int currentPageNo) throws SQLException, ClassNotFoundException {
        Connection connection = BaseDao.getConnection();
        List<User> userList = new ArrayList<User>();
        userList = userDao.getUserList(connection, userName, userRole, pageSize, currentPageNo);
        connection.close();

        return userList;
    }

    public boolean getUserCodeExist(String userCode) throws SQLException, ClassNotFoundException {
        Connection connection = BaseDao.getConnection();
        boolean flag = userDao.getUserCodeExist(connection, userCode);
        connection.close();
        return flag;
    }

    public boolean addUser(User user) throws SQLException, ClassNotFoundException {
        boolean flag = false;
        int insertRows;
        Connection connection = null;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务管理
            insertRows = userDao.addUser(connection, user);
            connection.commit();
            if (insertRows > 0){
                flag = true;
            }
        }catch(Exception e){
            connection.rollback();
        }finally {
            connection.close();
        }
        return flag;

    }

    public boolean delUser(int userId) throws SQLException, ClassNotFoundException {
        Connection connection = BaseDao.getConnection();
        int delRows = 0;
        boolean flag = false;
        delRows = userDao.delUser(connection, userId);
        if (delRows>0){
            flag = true;
        }
        connection.close();
        return flag;
    }

    public User getUserById(int userId) throws SQLException, ClassNotFoundException {
        Connection connection = BaseDao.getConnection();
        User user = null;
        user = userDao.getUserById(connection, userId);
        return user;
    }

    public boolean modifyUser(User user) throws SQLException, ClassNotFoundException {
        Connection connection = BaseDao.getConnection();
        boolean flag = false;
        int updateRows = userDao.modifyUser(connection, user);
        if (updateRows>0){
            flag = true;
        }
        return flag;
    }



}