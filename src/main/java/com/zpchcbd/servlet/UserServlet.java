package com.zpchcbd.servlet;

import com.alibaba.fastjson.JSONArray;
import com.zpchcbd.pojo.User;
import com.zpchcbd.service.user.UserServiceImpl;
import com.zpchcbd.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
        }
    }

    private void savePwd(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ClassNotFoundException, ServletException, IOException {
        String oldPwd = req.getParameter("oldpassword");
        String newPwd = req.getParameter("newpassword");
        String rNewPwd = req.getParameter("rnewpassword");
        boolean flag = false;
        User user = (User) req.getSession().getAttribute(Constants.USERSESSION);
        if (!oldPwd.equals(newPwd) && !oldPwd.equals(rNewPwd)
//            && !com.mysql.jdbc.StringUtils.isNullOrEmpty(oldPwd)
            && !com.mysql.jdbc.StringUtils.isNullOrEmpty(newPwd)
            && !com.mysql.jdbc.StringUtils.isNullOrEmpty(rNewPwd)
        ){
            if (newPwd.equals(rNewPwd)){
                flag = new UserServiceImpl().updatePwd(user.getUserCode(),newPwd);
                if(flag){
                    req.setAttribute("message", "修改密码成功,请退出并使用新密码重新登录！");
                    req.getSession().removeAttribute(Constants.USERSESSION);
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
        User user = (User) req.getSession().getAttribute(Constants.USERSESSION);
        Map<String, String> hashMap = new HashMap<String, String>();

        // 判断旧密码的情况
        if(user == null){
            hashMap.put("result", "sessionerror");
        }else if(com.mysql.jdbc.StringUtils.isNullOrEmpty(oldPwd)){
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
}
