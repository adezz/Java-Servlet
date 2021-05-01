package com.zpchcbd.dao.Role;

import com.zpchcbd.pojo.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleDao {
    public List<Role> getRoleList() throws SQLException, ClassNotFoundException;
}
