package com.zpchcbd.servlet;

import com.zpchcbd.dao.BaseDao;
import com.zpchcbd.pojo.User;
import com.zpchcbd.service.user.UserService;
import com.zpchcbd.service.user.UserServiceImpl;
import com.zpchcbd.utils.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("userCode");
        String password = req.getParameter("userPassword");
        User user = null;
        try {
            user = new UserServiceImpl().login(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (user != null){
            req.getSession().setAttribute(Constants.USERSESSION, user);
            resp.sendRedirect( req.getContextPath() + "/jsp/frame.jsp");
        }else{
            req.setAttribute("error", "用户名或密码不正确");
            req.getRequestDispatcher(req.getContextPath() + "/login.jsp").forward(req, resp);
        }
    }
}
