package com.zpchcbd.dao.user;

import com.zpchcbd.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao {
    public User getLoginUser(String userCode) throws SQLException, ClassNotFoundException;
    public int updatePwd(String username, String pwd) throws SQLException, ClassNotFoundException;
    public int getUserSize(String username, int userRole) throws SQLException, ClassNotFoundException;
}
