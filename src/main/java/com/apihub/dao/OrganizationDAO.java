package com.apihub.dao;

import com.apihub.model.Organization;

import java.sql.*;

public class OrganizationDAO {

    private Connection con;

    public OrganizationDAO(Connection con) {
        this.con = con;
    }

    public long insert(Organization org) throws Exception {

        String sql =
                "INSERT INTO organizations(name,email,status,created_at) " +
                "VALUES(?,?,?,?)";

        try (PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, org.getName());
            ps.setString(2, org.getEmail());
            ps.setString(3, org.getStatus());
            ps.setTimestamp(4, Timestamp.valueOf(org.getCreatedAt()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        return -1;
    }
}
