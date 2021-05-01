package com.zpchcbd.filter;

import com.zpchcbd.pojo.User;
import com.zpchcbd.utils.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        User user = (User) httpServletRequest.getSession().getAttribute(Constants.USER_SESSION);
        if (user == null){
            httpServletResponse.sendRedirect( httpServletRequest.getContextPath() + "/error.jsp");
        }else {
            chain.doFilter(request,response);
        }
    }

    public void destroy() {

    }
}
