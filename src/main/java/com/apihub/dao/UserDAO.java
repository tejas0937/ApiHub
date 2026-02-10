package com.apihub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.apihub.model.User;

public class UserDAO {

    private Connection con;

    public UserDAO(Connection con) {
        this.con = con;
    }

    // Login
    public User findByEmailAndPassword(String email, String password) throws Exception {

        String sql =
                "SELECT id, organization_id, full_name, email, status " +
                "FROM users WHERE email = ? AND password_hash = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User u = new User();
                    u.setId(rs.getLong("id"));
                    u.setOrganizationId(rs.getLong("organization_id"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    u.setStatus(rs.getString("status"));

                    return u;
                }
            }
        }

        return null;
    }
    public void insert(User user) throws Exception {

        String sql =
            "INSERT INTO users " +
            "(organization_id, full_name, email, password_hash, status) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps =
                 con.prepareStatement(sql)) {

            ps.setLong(1, user.getOrganizationId());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getStatus());

            ps.executeUpdate();
        }
    }

    // User roles
    public List<String> findRolesByUserId(Long userId) throws Exception {

        List<String> roles = new ArrayList<>();

        String sql =
                "SELECT r.name " +
                "FROM roles r " +
                "JOIN user_roles ur ON r.id = ur.role_id " +
                "WHERE ur.user_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    roles.add(rs.getString("name"));
                }
            }
        }

        return roles;
    }
    public void assignRole(String email, String roleName) throws Exception {

        String findUserSql =
            "SELECT id FROM users WHERE email = ?";

        long userId;

        try (PreparedStatement ps = con.prepareStatement(findUserSql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new Exception("User not found for role assignment");
                }

                userId = rs.getLong("id");
            }
        }

        String findRoleSql =
            "SELECT id FROM roles WHERE name = ?";

        long roleId;

        try (PreparedStatement ps = con.prepareStatement(findRoleSql)) {

            ps.setString(1, roleName);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new Exception("Role not found: " + roleName);
                }

                roleId = rs.getLong("id");
            }
        }

        String insertSql =
            "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(insertSql)) {

            ps.setLong(1, userId);
            ps.setLong(2, roleId);

            ps.executeUpdate();
        }
    }

    // List employees by organization
    public List<User> findByOrganization(long orgId) throws Exception {

        List<User> list = new ArrayList<>();

        String sql =
                "SELECT id, organization_id, full_name, email, status " +
                "FROM users WHERE organization_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orgId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    User u = new User();
                    u.setId(rs.getLong("id"));
                    u.setOrganizationId(rs.getLong("organization_id"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    u.setStatus(rs.getString("status"));

                    list.add(u);
                }
            }
        }

        return list;
    }
}
