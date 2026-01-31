package com.example.demo.service;

import com.example.demo.dao.OrganizationDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.model.Organization;
import com.example.demo.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserService {

    private final UserDAO userDAO = new UserDAO();
    private final OrganizationDAO organizationDAO = new OrganizationDAO();

    // ----------------------------------------
    // Register a new company + admin user
    // ----------------------------------------
    public void registerOrganizationWithAdmin(
            String orgName,
            String orgEmail,
            String adminName,
            String adminEmail,
            String password
    ) throws Exception {

        // 1. Create organization object
        Organization org = new Organization();
        org.setName(orgName);
        org.setEmail(orgEmail);
        org.setStatus("ACTIVE");
        org.setCreatedAt(LocalDateTime.now());

        // 2. Save organization
        int orgId = organizationDAO.createOrganization(org);

        if (orgId <= 0) {
            throw new RuntimeException("Failed to create organization");
        }

        // 3. Create admin user for that organization
        User user = new User();
        user.setOrganizationId(orgId);
        user.setFullName(adminName);
        user.setEmail(adminEmail);
        user.setPasswordHash(hashPassword(password));
        user.setRole("ORG_ADMIN");
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());

        userDAO.createUser(user);
    }

    // ----------------------------------------
    // Create platform super admin
    // ----------------------------------------
    public void createSuperAdmin(
            String name,
            String email,
            String password
    ) throws Exception {

        User user = new User();
        user.setOrganizationId(null); // important
        user.setFullName(name);
        user.setEmail(email);
        user.setPasswordHash(hashPassword(password));
        user.setRole("SUPER_ADMIN");
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());

        userDAO.createUser(user);
    }

    // ----------------------------------------
    // Login
    // ----------------------------------------
    public User login(String email, String password) throws Exception {

        User user = userDAO.findByEmail(email);

        if (user == null) {
            return null;
        }

        String hashedInput = hashPassword(password);

        if (!hashedInput.equals(user.getPasswordHash())) {
            return null;
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            return null;
        }

        return user;
    }

    // ----------------------------------------
    // Password hashing (simple, for project)
    // ----------------------------------------
    private String hashPassword(String password) throws Exception {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8)
        );

        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
