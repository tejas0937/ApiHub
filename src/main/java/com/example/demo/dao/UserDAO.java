package com.example.demo.dao;

import com.example.demo.model.User;
import com.example.demo.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // ----------------------------------
    // CREATE USER
    // ----------------------------------
    public int createUser(User user) throws SQLException {

        String sql = "INSERT INTO users " +
                "(organization_id, full_name, email, password_hash, role, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (user.getOrganizationId() == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, user.getOrganizationId());
            }

            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getStatus());
            ps.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }

    // ----------------------------------
    // FIND USER BY EMAIL
    // (used for login)
    // ----------------------------------
    public User findByEmail(String email) throws SQLException {

        String sql = "SELECT id, organization_id, full_name, email, password_hash, " +
                     "role, status, created_at " +
                     "FROM users WHERE email = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        }

        return null;
    }

    // ----------------------------------
    // LIST USERS BY ORGANIZATION
    // (company admin view)
    // ----------------------------------
    public List<User> findByOrganization(int organizationId) throws SQLException {

        String sql = "SELECT id, organization_id, full_name, email, password_hash, " +
                     "role, status, created_at " +
                     "FROM users WHERE organization_id = ?";

        List<User> users = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, organizationId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    users.add(mapRowToUser(rs));
                }
            }
        }

        return users;
    }

    // ----------------------------------
    // INTERNAL MAPPER METHOD
    // ----------------------------------
    private User mapRowToUser(ResultSet rs) throws SQLException {

        User user = new User();

        user.setId(rs.getInt("id"));

        int orgId = rs.getInt("organization_id");
        if (rs.wasNull()) {
            user.setOrganizationId(null);
        } else {
            user.setOrganizationId(orgId);
        }

        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            user.setCreatedAt(ts.toLocalDateTime());
        }

        return user;
    }
}
