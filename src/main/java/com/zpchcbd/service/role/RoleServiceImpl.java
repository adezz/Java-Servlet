package com.zpchcbd.service.role;

import com.zpchcbd.dao.Role.RoleDao;
import com.zpchcbd.dao.Role.RoleDaoImpl;
import com.zpchcbd.pojo.Role;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService{

    private RoleDaoImpl roleDao = null;

    public RoleServiceImpl(){
        roleDao = new RoleDaoImpl();
    }


    public List<Role> getRoleList() throws SQLException, ClassNotFoundException {
        List<Role> roleList;
        roleList = roleDao.getRoleList();
        return roleList;
    }
}
