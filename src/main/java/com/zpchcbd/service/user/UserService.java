package com.zpchcbd.service.user;

import com.zpchcbd.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    public User login(String userCode, String userPassword) throws SQLException, ClassNotFoundException;

    public boolean updatePwd(String username, String password) throws SQLException, ClassNotFoundException;

    public int getUserSize(String username, int userRole) throws SQLException, ClassNotFoundException;

    public List<User> getUserList(String userName, int userRole, int pageSize, int currentPageNo) throws SQLException, ClassNotFoundException;

    public boolean getUserCodeExist(String userCode) throws SQLException, ClassNotFoundException;

    public boolean addUser(User user) throws SQLException, ClassNotFoundException;

    public boolean delUser(int userId) throws SQLException, ClassNotFoundException;

    public User getUserById(int userId) throws SQLException, ClassNotFoundException;

    public boolean modifyUser(User user) throws SQLException, ClassNotFoundException;
}