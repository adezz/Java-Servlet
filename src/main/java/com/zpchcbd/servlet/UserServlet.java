package com.zpchcbd.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xerces.internal.xs.ItemPSVI;
import com.zpchcbd.pojo.Role;
import com.zpchcbd.pojo.User;
import com.zpchcbd.service.role.RoleService;
import com.zpchcbd.service.role.RoleServiceImpl;
import com.zpchcbd.service.user.UserService;
import com.zpchcbd.service.user.UserServiceImpl;
import com.zpchcbd.utils.Constants;
import com.zpchcbd.utils.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mysql.jdbc.StringUtils.dumpAsHex;
import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("pwdmodify")){
            this.pwdModify(req, resp);
        }else if(method.equals("savepwd")){
            try {
                this.savePwd(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }else if(method.equals("query")){
            try {
                this.query(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else if (method.equals("getrolelist")){
            System.out.println("getrolelist's method...");
            try {
                this.getrolelist(req,resp);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else if(method.equals("add")){
            try {
                System.out.println("============addUser===========");
                this.addUser(req,resp);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }else if(method.equals("ucexist")) {
            try {
                this.ucexist(req,resp);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else if(method.equals("deluser")){
            try {
                this.delUser(req,resp);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else if(method.equals("modify")){
            try {
                this.modify(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else if(method.equals("view")){
            try {
                this.view(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else if(method.equals("modifyexe")){
            try {
                this.modifyexe(req, resp);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePwd(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, ServletException, IOException {
        String oldPwd = req.getParameter("oldpassword");
        String newPwd = req.getParameter("newpassword");
        String rNewPwd = req.getParameter("rnewpassword");
        boolean flag = false;
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        if (!oldPwd.equals(newPwd) && !oldPwd.equals(rNewPwd)
//            && !com.mysql.jdbc.StringUtils.isNullOrEmpty(oldPwd)
            && !isNullOrEmpty(newPwd)
            && !isNullOrEmpty(rNewPwd)
        ){
            if (newPwd.equals(rNewPwd)){
                flag = new UserServiceImpl().updatePwd(user.getUserCode(),newPwd);
                if(flag){
                    req.setAttribute("message", "修改密码成功,请退出并使用新密码重新登录！");
                    req.getSession().removeAttribute(Constants.USER_SESSION);
                }else{
                    req.setAttribute("message", "密码修改失败");
                }
            }
        }else{
            req.setAttribute("message", "修改密码有问题！");
        }
        req.getRequestDispatcher(req.getContextPath() + "/jsp/pwdmodify.jsp").forward(req, resp);
    }

    // 验证旧密码是否正确
    private void pwdModify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oldPwd = req.getParameter("oldpassword");
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        Map<String, String> hashMap = new HashMap<String, String>();

        // 判断旧密码的情况
        if(user == null){
            hashMap.put("result", "sessionerror");
        }else if(isNullOrEmpty(oldPwd)){
            hashMap.put("result", "error");
        }else{
            if (user.getUserPassword().equals(oldPwd)) {
                hashMap.put("result", "true");
            }else{
                hashMap.put("result", "false");
            }
        }

        // 转换json
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(hashMap));
        writer.flush();
        writer.close();
    }

    // 查询用户信息
    private void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException, ClassNotFoundException {
        PageSupport pageSupport = new PageSupport();
        UserService userService = new UserServiceImpl();
        RoleService roleService = new RoleServiceImpl();
        List<User> userList = null;
        List<Role> roleList = null;
        String tempQueryUserRole = null; //默认为0
        String tempPageIndex = null;
        String queryName = null;

        //获取参数
        queryName = req.getParameter("queryname");
        tempQueryUserRole = req.getParameter("queryUserRole");
        tempPageIndex = req.getParameter("pageIndex");

        if (queryName == null){
            queryName = "";
        }

        int queryUserRole = 0;
        if (tempQueryUserRole == null || tempQueryUserRole.equals("")){
            queryUserRole = 0;
        }else{
            queryUserRole = Integer.parseInt(tempQueryUserRole);
        }

        int pageIndex = 0;
        if (tempPageIndex == null){
            pageIndex = 0;
        }else{
            pageIndex = Integer.parseInt(tempPageIndex);
        }


        //获取用户数量
        int userNumber = userService.getUserSize(queryName, queryUserRole);

        System.out.println("userNumber: " + userNumber);
        //计算分页
        pageSupport.setPageSize(Constants.pageSize);
        pageSupport.setTotalCount(userNumber);
        pageSupport.setCurrentPageNo(1);

        //控制首页和尾页
        if(pageIndex < 1){
            pageIndex = 1;
        }else if(pageIndex > pageSupport.getTotalPageCount()){
            pageIndex = pageSupport.getTotalPageCount();
        }

        //获取用户列表
        userList = userService.getUserList(queryName, queryUserRole, pageSupport.getPageSize(), pageIndex);

        //获取角色列表
        roleList = roleService.getRoleList();

        //DEBUG
        System.out.println("queryUserName servlet-------->" + queryName);
        System.out.println("queryUserRole servlet-------->" + queryUserRole);
        System.out.println("query pageIndex---------> " + pageIndex);

        //渲染界面数据
        req.setAttribute("userList", userList);
        req.setAttribute("roleList", roleList);
        req.setAttribute("totalCount", pageSupport.getTotalCount());
        req.setAttribute("currentPageNo", pageIndex);
        req.setAttribute("totalPageCount", pageSupport.getTotalPageCount());
        req.setAttribute("queryUserName", queryName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.getRequestDispatcher(  req.getContextPath() + "/jsp/userlist.jsp").forward(req, resp);

    }

    // 获取角色列表
    private void getrolelist(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, IOException {
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(roleList));
        writer.flush();
        writer.close();
    }

    // 添加账户时，检查账户是否已经存在
    private void ucexist(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, IOException {
        String userCode = req.getParameter("userCode");
        Map<String, String> hashMap = new HashMap<String, String>();

        //一些非法的先直接过滤了，正常的再传入进行判断是否存在
        if (userCode.equals("") || userCode == null){
            hashMap.put("userCode", "exist");
        }else{
            // 数据库进行查询是否存在
            UserService userService = new UserServiceImpl();
            boolean flag = userService.getUserCodeExist(userCode);
            if (flag == true){
                hashMap.put("userCode", "exist");
            }else{
                hashMap.put("userCode","noexist");
            }
        }

        // 界面数据回传
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(hashMap));
        System.out.println( "getrolelist: " + JSONArray.toJSONString(hashMap));
        writer.flush();
        writer.close();
    }

    // 添加用户信息
    private void addUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException, ClassNotFoundException{
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setGender(Integer.parseInt(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.parseInt(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());

        UserService userService = new UserServiceImpl();
        if(userService.addUser(user)){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        }
    }

    // 用户管理删除用户
    public void delUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, IOException, ServletException {
        String userId = req.getParameter("uid");
        Map<String, String> hashMap = new HashMap<String, String>();
        UserService userService = new UserServiceImpl();
        System.out.println("userCode:" + Integer.parseInt(userId));
        if (Integer.parseInt(userId) != 0){
            if(userService.delUser(Integer.parseInt(userId))){
                // 删除成功
                System.out.println("usercode 删除成功...");
                hashMap.put("delResult", "true");
            }else{
                // 删除失败
                System.out.println("usercode 删除失败");
                hashMap.put("delResult", "false");
            }
        }else{
            // userCode非法
            System.out.println("usercode 非法...");
            hashMap.put("delResult", "false");
        }

        // 界面数据回传
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(hashMap));
        System.out.println( "getrolelist: " + JSONArray.toJSONString(hashMap));
        writer.flush();
        writer.close();
    }

    public void modify(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, ServletException, IOException {
        User user = null;
        String userId = req.getParameter("uid");
        user = new UserServiceImpl().getUserById(Integer.parseInt(userId));
        req.setAttribute("user", user);
        req.getRequestDispatcher("/jsp/usermodify.jsp").forward(req, resp);
    }

    public void modifyexe(HttpServletRequest req, HttpServletResponse resp) throws ParseException, SQLException, ClassNotFoundException, IOException {
        User user = new User();

        String userId = req.getParameter("uid");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        String phone = req.getParameter("phone");
        String gender = req.getParameter("gender");
        String userName = req.getParameter("userName");
        String birthday = req.getParameter("birthday");
        Date date = null;

        System.out.println("userId ->" + userId);
        System.out.println("address ->" + address);
        System.out.println("userRole ->" + userRole);
        System.out.println("phone ->" + phone);
        System.out.println("gender ->" + gender);
        System.out.println("userName ->" + userName);
        System.out.println("birthday ->" + birthday);

        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        user.setId(Integer.parseInt(userId));
        user.setUserName(userName);
        user.setAddress(address);
        user.setPhone(phone);
        user.setGender(Integer.parseInt(gender));
        user.setBirthday(date);
        user.setUserRole(Integer.parseInt(userRole));

        UserServiceImpl userService = new UserServiceImpl();
        userService.modifyUser(user);
        resp.sendRedirect("/jsp/user.do?method=query");
    }

    public void view(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, ServletException, IOException {
        User user = null;
        String userId = req.getParameter("uid");
        user = new UserServiceImpl().getUserById(Integer.parseInt(userId));
        req.setAttribute("user", user);
        req.getRequestDispatcher("/jsp/userview.jsp").forward(req, resp);
    }


}
