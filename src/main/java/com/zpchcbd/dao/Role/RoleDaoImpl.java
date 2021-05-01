package com.zpchcbd.dao.Role;

import com.zpchcbd.dao.BaseDao;
import com.zpchcbd.pojo.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {

    // 获取用户角色列表
    public List<Role> getRoleList() throws SQLException, ClassNotFoundException {
        List<Role> roleList = new ArrayList<Role>();
        Connection connection = BaseDao.getConnection();

        String sql = "select * from smbms_role;";
        Object[] params = {};

        ResultSet resultSet = BaseDao.execute(connection,sql,params);
        while(resultSet.next()){
            Role role = new Role();
            role.setId(resultSet.getInt("id"));
            role.setRoleCode(resultSet.getString("roleCode"));
            role.setRoleName(resultSet.getString("roleName"));
            roleList.add(role);
        }

        return roleList;
    }
}
