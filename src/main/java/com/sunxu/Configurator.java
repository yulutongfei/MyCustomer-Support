package com.sunxu;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author 孙许
 * @date 2018/03/01
 * @description 编程式配置过滤器
 */
@WebListener
public class Configurator implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        FilterRegistration.Dynamic registration = context.addFilter("authenticationFilter", new AuthenticationFilter());
        registration.setAsyncSupported(true);
        registration.addMappingForUrlPatterns(null, false, "/sessions", "/tickets");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
