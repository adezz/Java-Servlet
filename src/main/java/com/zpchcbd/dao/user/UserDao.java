package com.zpchcbd.dao.user;

import com.zpchcbd.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface UserDao {
    public User getLoginUser(Connection connection, String userCode) throws SQLException, ClassNotFoundException;

    public int updatePwd(Connection connection, String username, String pwd) throws SQLException, ClassNotFoundException;

    public int getUserSize(Connection connection, String username, int userRole) throws SQLException, ClassNotFoundException;

    public List<User> getUserList(Connection connection, String userName, int userRole, int pageSize, int currentPageNo) throws SQLException, ClassNotFoundException;

    public boolean getUserCodeExist(Connection connection, String userCode) throws SQLException, ClassNotFoundException;

    public int addUser(Connection connection, User user) throws SQLException, ClassNotFoundException;

    public int delUser(Connection connection, int userId) throws SQLException;

    public int modifyUser(Connection connection, User user) throws SQLException;

    public User getUserById(Connection connection, int userId) throws SQLException;
}