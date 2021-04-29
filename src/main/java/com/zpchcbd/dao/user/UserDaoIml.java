package com.zpchcbd.dao.user;

import com.zpchcbd.dao.BaseDao;
import com.zpchcbd.pojo.User;
import com.zpchcbd.utils.Constants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoIml implements UserDao {
    // Dao:后台登录验证
    public User getLoginUser(String userCode) throws SQLException, ClassNotFoundException {
        Object[] params = {userCode};
        User user = new User();
        Connection connection = BaseDao.getConnection();
        ResultSet resultSet = BaseDao.execute(connection, "select * from smbms_user where userCode=?", params);
        if(resultSet.next()){
            user.setId(resultSet.getInt("id"));
            user.setUserCode(resultSet.getString("UserCode"));
            user.setUserName(resultSet.getString("UserName"));
            user.setUserPassword(resultSet.getString("UserPassword"));
            user.setGender(resultSet.getInt("Gender"));
            user.setBirthday(resultSet.getDate("Birthday"));
            user.setPhone(resultSet.getString("Phone"));
            user.setAddress(resultSet.getString("Address"));
            user.setUserRole(resultSet.getInt("UserRole"));
            user.setCreatedBy(resultSet.getInt("CreatedBy"));
            user.setCreationDate(resultSet.getDate("CreationDate"));
            user.setModifyBy(resultSet.getInt("ModifyBy"));
            user.setModifyDate(resultSet.getDate("ModifyDate"));
        }

        return user;

    }

    // Dao:修改用户密码
    public int updatePwd(String username, String pwd) throws SQLException, ClassNotFoundException {
        int flag = 0;
        Connection connection = BaseDao.getConnection();
        String sql = "update smbms_user set userPassword= ? where userCode = ?";
        Object[] params = {pwd,username};
        flag = BaseDao.update(connection, sql, params);
        return flag;
    }

    // Dao:获取查询用户数量
    public int getUserSize(String username, int userRole) throws SQLException, ClassNotFoundException{
        Connection connection = BaseDao.getConnection();

        int Size = 0;
        List<Object> arrayList = new ArrayList<Object>();
        StringBuffer stringBuffer = new StringBuffer("select count(*) from smbms_user a,smbms_role b where a.userRole = b.id;");
        if (com.mysql.jdbc.StringUtils.isNullOrEmpty(username)){
            arrayList.add(username);
            stringBuffer.append(" and username='%?%'");
        }

        if(userRole != 0){
            arrayList.add(userRole);
            stringBuffer.append(" and a.userRole = '?'");
        }

        System.out.println("SQL: " + stringBuffer.toString());

        ResultSet resultSet = BaseDao.execute(connection, stringBuffer.toString(), arrayList.toArray());
        if(resultSet.next()){
            Size = resultSet.getInt(1);
        }

        return Size;
    }

}
