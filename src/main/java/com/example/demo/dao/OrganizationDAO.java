package com.example.demo.dao;

import com.example.demo.model.Organization;
import com.example.demo.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO {

    // INSERT ORGANIZATION
   
    public int createOrganization(Organization organization) throws SQLException {

        String sql = "INSERT INTO organizations (name, email, status, created_at) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, organization.getName());
            ps.setString(2, organization.getEmail());
            ps.setString(3, organization.getStatus());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(organization.getCreatedAt()));

            ps.executeUpdate();

           
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }

    // FIND ORGANIZATION BY ID

    public Organization findById(int id) throws SQLException {

        String sql = "SELECT id, name, email, status, created_at " +
                     "FROM organizations WHERE id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Organization org = new Organization();
                    org.setId(rs.getInt("id"));
                    org.setName(rs.getString("name"));
                    org.setEmail(rs.getString("email"));
                    org.setStatus(rs.getString("status"));

                    java.sql.Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        org.setCreatedAt(ts.toLocalDateTime());
                    }

                    return org;
                }
            }
        }

        return null;
    }

    // LIST ALL ORGANIZATIONS
    // (Super Admin use case)

    public List<Organization> findAll() throws SQLException {

        String sql = "SELECT id, name, email, status, created_at FROM organizations";

        List<Organization> list = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Organization org = new Organization();
                org.setId(rs.getInt("id"));
                org.setName(rs.getString("name"));
                org.setEmail(rs.getString("email"));
                org.setStatus(rs.getString("status"));

                java.sql.Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    org.setCreatedAt(ts.toLocalDateTime());
                }

                list.add(org);
            }
        }

        return list;
    }
}
