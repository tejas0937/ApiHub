package com.apihub.dao;

import com.apihub.model.User;

import java.sql.*;

public class UserDAO {

    private Connection con;

    public UserDAO(Connection con) {
        this.con = con;
    }

    public long insert(User user) throws Exception {

        String sql =
                "INSERT INTO users(organization_id,full_name,email,password_hash,status,created_at) " +
                "VALUES(?,?,?,?,?,?)";

        try (PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (user.getOrganizationId() == null) {
                ps.setNull(1, Types.BIGINT);
            } else {
                ps.setLong(1, user.getOrganizationId());
            }

            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getStatus());
            ps.setTimestamp(6, Timestamp.valueOf(user.getCreatedAt()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }

        return -1;
    }

    public User findByEmail(String email) throws Exception {

        String sql =
                "SELECT id,organization_id,full_name,email,password_hash,status,created_at " +
                "FROM users WHERE email=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User u = new User();

                    u.setId(rs.getLong("id"));

                    long orgId = rs.getLong("organization_id");
                    if (!rs.wasNull()) {
                        u.setOrganizationId(orgId);
                    }

                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setStatus(rs.getString("status"));

                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        u.setCreatedAt(ts.toLocalDateTime());
                    }

                    return u;
                }
            }
        }

        return null;
    }
}
