package com.sunxu;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author 孙许
 * @date 2018/03/01
 * @description 登陆权限过滤器，如果session不为null并且username为空则跳转到登陆页面
 */
public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        if (session != null && session.getAttribute("username") == null) {
            ((HttpServletResponse) response).sendRedirect("login");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
