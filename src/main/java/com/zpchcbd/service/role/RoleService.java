package com.zpchcbd.service.role;

import com.zpchcbd.pojo.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleService {
    public List<Role> getRoleList() throws SQLException, ClassNotFoundException;
}
