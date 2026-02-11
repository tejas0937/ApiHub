package com.apihub.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apihub.dao.ApiKeyDAO;
import com.apihub.dao.ApiUsageLogDAO;
import com.apihub.model.ApiKey;
import com.apihub.model.ApiUsageLog;
import com.apihub.util.DBUtil;

public class DemoApiTest extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Allow BOTH GET and POST for demo
        handle(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String apiKey = req.getHeader("MyApi");

        if (apiKey == null || apiKey.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Missing API key");
            return;
        }

        try (Connection con = DBUtil.getConnection()) {

            ApiKeyDAO keyDao = new ApiKeyDAO(con);
            ApiKey key = keyDao.findByKey(apiKey);

            if (key == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("Invalid API key");
                return;
            }

            ApiUsageLog log = new ApiUsageLog();
            log.setApiKeyId(key.getId());
            log.setApiName("Demo");

            ApiUsageLogDAO usageDao = new ApiUsageLogDAO(con);
            usageDao.insert(log);

            resp.setContentType("application/json");
            resp.getWriter().write(
                "{ \"message\": \"Api processed successfully\" }"
            );

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("Server error");
        }
    }
}
