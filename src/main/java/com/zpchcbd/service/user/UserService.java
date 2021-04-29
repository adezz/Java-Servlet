package com.zpchcbd.service.user;

import com.zpchcbd.pojo.User;

import java.sql.SQLException;

public interface UserService {
    public User login(String userCode, String userPassword) throws SQLException, ClassNotFoundException;

    public boolean updatePwd(String username, String password) throws SQLException, ClassNotFoundException;

    public int getUserSize(String username, int userRole) throws SQLException, ClassNotFoundException;
}
