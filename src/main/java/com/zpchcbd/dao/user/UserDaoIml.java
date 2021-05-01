package com.zpchcbd.dao.user;

import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import com.zpchcbd.dao.BaseDao;
import com.zpchcbd.pojo.User;
import com.zpchcbd.utils.Constants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDaoIml implements UserDao {
    // Dao:后台登录验证
    public User getLoginUser(Connection connection, String userCode) throws SQLException, ClassNotFoundException {
        Object[] params = {userCode};
        User user = new User();
        ResultSet resultSet = BaseDao.execute(connection, "select * from smbms_user where userCode=?", params);
        if (resultSet.next()) {
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
    public int updatePwd(Connection connection, String username, String pwd) throws SQLException, ClassNotFoundException {
        int flag = 0;
        String sql = "update smbms_user set userPassword= ? where userCode = ?";
        Object[] params = {pwd, username};
        flag = BaseDao.update(connection, sql, params);
        return flag;
    }

    // Dao:获取查询用户数量
    public int getUserSize(Connection connection, String userName, int userRole) throws SQLException, ClassNotFoundException {
        int Size = 0;
        List<Object> arrayList = new ArrayList<Object>();
        StringBuffer stringBuffer = new StringBuffer("select count(*) from smbms_user a,smbms_role b where a.userRole = b.id");
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(userName)) {
            arrayList.add("%" + userName + "%");
            stringBuffer.append(" and a.userName like ?");
        }

        if (userRole > 0) {
            arrayList.add(userRole);
            stringBuffer.append(" and a.userRole = ?");
        }

        System.out.println("getUserSize: " + stringBuffer.toString());

        ResultSet resultSet = BaseDao.execute(connection, stringBuffer.toString(), arrayList.toArray());
        if (resultSet.next()) {
            Size = resultSet.getInt(1);
        }

        return Size;
    }

    // Dao:获取用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int pageSize, int currentPageNo) throws SQLException, ClassNotFoundException {
        List<User> userList = new ArrayList<User>();
        List<Object> params = new ArrayList<Object>();

        StringBuffer stringBuffer= new StringBuffer("select * from smbms_user a,smbms_role b where a.userRole = b.id");

        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(userName)){
            stringBuffer.append(" and a.userName like ?");
            params.add("%"+userName+"%");
        }

        if (userRole > 0){
            stringBuffer.append(" and a.userRole=?");
            params.add(userRole);
        }

        stringBuffer.append(" order by a.createdBy DESC limit ?,?");
        currentPageNo = (currentPageNo-1)*pageSize;
        // stringBuffer.append(" order by CreateBy limit ?,?");
        params.add(currentPageNo);
        params.add(pageSize);

        System.out.println("getUserList: " + stringBuffer.toString());

        ResultSet resultSet = BaseDao.execute(connection, stringBuffer.toString(), params.toArray());
        while(resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setUserCode(resultSet.getString("userCode"));
            user.setUserName(resultSet.getString("userName"));
            user.setGender(resultSet.getInt("gender"));
            user.setBirthday(resultSet.getDate("birthday"));
            user.setPhone(resultSet.getString("phone"));
            user.setUserRole(resultSet.getInt("userRole"));
            userList.add(user);
        }
        return userList;

    }

    // Dao:添加用户时，检查用户名是否存在
    public boolean getUserCodeExist(Connection connection, String userCode) throws SQLException, ClassNotFoundException{
        String sql = "select * from smbms_user where userCode = ? limit 0,1";
        Object[] params = {userCode};
        ResultSet resultSet = BaseDao.execute(connection, sql, params);
        int flagId = 0;
        if (resultSet.next()){
            flagId = resultSet.getInt("id");
        }

        if (flagId != 0){
            return true;
        }else{
            return false; // false 则为不存在
        }
    }

    // Dao:添加用户
    public int addUser(Connection connection, User user) throws SQLException, ClassNotFoundException {
        int insertRows = 0;
        String sql = "insert into smbms_user (userCode,userName,userPassword," +
                "userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                "values(?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(), user.getUserRole(),user.getGender(),user.getBirthday(),
                user.getPhone(),user.getAddress(),user.getCreationDate(),user.getCreatedBy()
        };

        insertRows = BaseDao.insert(connection, sql, params);
        return insertRows;
    }

    public int delUser(Connection connection, int UserId) throws SQLException {
        int delRows = 0;
        String sql = "delete from smbms_user where id = ?";
        Object[] params = {UserId};
        delRows = BaseDao.delete(connection, sql, params);
        return delRows;
    }

    public int modifyUser(Connection connection, User user) throws SQLException {
        int updateRows = 0;
        String sql = "update smbms_user set " +
                "userName=?," +
                "gender=?," +
                "birthday=?," +
                "phone=?," +
                "address=?," +
                "userRole=? where id = ?";
        Object[] params = {user.getUserName(), user.getGender(),user.getBirthday(),
                user.getPhone(),user.getAddress(),user.getUserRole(),user.getId()};
        updateRows = BaseDao.update(connection, sql, params);
        return updateRows;
    }

    public User getUserById(Connection connection, int userId) throws SQLException {
        User user = new User();
        String sql = "select * from smbms_user where id = ?;";
        Object[] params = {userId};
        ResultSet resultSet = BaseDao.execute(connection, sql, params);
        if (resultSet.next()){
            user.setId(resultSet.getInt("id"));
            user.setUserCode(resultSet.getString("userCode"));
            user.setUserName(resultSet.getString("userName"));
            user.setGender(resultSet.getInt("gender"));
            user.setBirthday(resultSet.getDate("birthday"));
            user.setPhone(resultSet.getString("phone"));
            user.setUserRole(resultSet.getInt("userRole"));
            user.setAddress(resultSet.getString("address"));
        }
        return user;
    }

}
