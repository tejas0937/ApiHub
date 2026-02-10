package com.apihub.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.apihub.dao.UserDAO;
import com.apihub.model.User;
import com.apihub.util.DBUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null) {
            resp.sendRedirect(req.getContextPath() + "/login-page");
            return;
        }

        try (Connection con = DBUtil.getConnection()) {

            UserDAO userDAO = new UserDAO(con);

            User user = userDAO.findByEmailAndPassword(email, password);

            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/login-page");
                return;
            }

            List<String> roles = userDAO.findRolesByUserId(user.getId());

            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("roles", roles);

            if (roles.contains("SUPER_ADMIN")) {

                req.getRequestDispatcher(
                        "/WEB-INF/views/superadmin/dashboard.jsp"
                ).forward(req, resp);

            } else if (roles.contains("ORG_ADMIN")) {

                req.getRequestDispatcher(
                        "/WEB-INF/views/org/dashboard.jsp"
                ).forward(req, resp);

            } else {

                req.getRequestDispatcher(
                        "/WEB-INF/views/index.jsp"
                ).forward(req, resp);

            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("Login failed due to server error.");
        }
    }
}
