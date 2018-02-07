package com.sunxu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author 孙许
 * @date 2018/02/07
 * @description
 */
@WebServlet(
        name = "LoginServlet",
        urlPatterns = "/login"
)
public class LoginServlet extends HttpServlet {

    private static final Map<String, String> userDatabase = new Hashtable<>();
    public static final Logger log = LogManager.getLogger();

    static {
        userDatabase.put("Nicholas", "password");
        userDatabase.put("Sarah", "drowssap");
        userDatabase.put("Sun", "sun342613");
        userDatabase.put("Xu", "monkeydluffy");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (request.getParameter("logout") != null) {
            session.invalidate();
            response.sendRedirect("login");
            return;
        } else if (session.getAttribute("username") != null) {
            response.sendRedirect("tickets");
            return;
        }
        request.setAttribute("loginFailed", false);
        request.getRequestDispatcher("/WEB-INF/jsp/view/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            response.sendRedirect("tickets");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean flag = username == null || password == null ||
                !LoginServlet.userDatabase.containsKey(username) ||
                !password.equals(LoginServlet.userDatabase.get(username));
        if (flag) {
            request.setAttribute("loginFailed", true);
            request.getRequestDispatcher("/WEB-INF/jsp/view/login.jsp").forward(request, response);
        } else {
            session.setAttribute("username", username);
            log.debug("sessionId1：" + session.getId());
            log.debug("username1：" + session.getAttribute("username"));
            request.changeSessionId();
            log.debug("sessionId2：" + session.getId());
            log.debug("username2：" + session.getAttribute("username"));
            response.sendRedirect("tickets");
        }
    }
}
