package com.apihub.servlet;

import com.apihub.service.OrganizationRegistrationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterOrganizationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrganizationRegistrationService service =
            new OrganizationRegistrationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        try {

            String orgName   = req.getParameter("orgName");
            String orgEmail  = req.getParameter("orgEmail");
            String adminName = req.getParameter("adminName");
            String adminEmail= req.getParameter("adminEmail");
            String password  = req.getParameter("password");

            if (orgName == null || orgEmail == null ||
                adminName == null || adminEmail == null ||
                password == null) {

                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Missing required fields");
                return;
            }

            // temporary hash (we will improve later)
            String passwordHash = Integer.toHexString(password.hashCode());

            service.registerOrganizationWithAdmin(
                    orgName,
                    orgEmail,
                    adminName,
                    adminEmail,
                    passwordHash
            );

            resp.getWriter().write("Registration successful");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Registration failed");
        }
    }
}
