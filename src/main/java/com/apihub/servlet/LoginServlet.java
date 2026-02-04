package com.apihub.servlet;

import com.apihub.model.User;
import com.apihub.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private LoginService loginService = new LoginService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");

        try {

            String email = req.getParameter("email");
            String password = req.getParameter("password");

            Map<String, Object> data =
                    loginService.login(email, password);

            if (data == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("Invalid credentials");
                return;
            }

            User user = (User) data.get("user");
            List<String> roles =
                    (List<String>) data.get("roles");

            HttpSession session = req.getSession(true);

            session.setAttribute("user", user);
            session.setAttribute("roles", roles);

            if (roles.contains("SUPER_ADMIN")) {

                resp.sendRedirect("org/SuperAdmindashboard.html");

            } else if (roles.contains("ORG_ADMIN")) {

                resp.sendRedirect("org/dashboard.html");

            } else {

                resp.sendRedirect("index.html");

            }


        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("Login error");
        }
    }
}
