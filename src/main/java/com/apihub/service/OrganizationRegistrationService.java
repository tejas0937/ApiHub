package com.apihub.service;

import com.apihub.dao.OrganizationDAO;
import com.apihub.dao.RoleDAO;
import com.apihub.dao.UserDAO;
import com.apihub.dao.UserRoleDAO;
import com.apihub.model.Organization;
import com.apihub.model.Role;
import com.apihub.model.User;
import com.apihub.util.DBUtil;

import java.sql.Connection;
import java.time.LocalDateTime;

public class OrganizationRegistrationService {

    public void registerOrganizationWithAdmin(
            String orgName,
            String orgEmail,
            String adminName,
            String adminEmail,
            String passwordHash
    ) throws Exception {

        Connection con = null;

        try {

            con = DBUtil.getConnection();
            con.setAutoCommit(false);   // start transaction

            OrganizationDAO orgDao = new OrganizationDAO(con);
            UserDAO userDao = new UserDAO(con);
            RoleDAO roleDao = new RoleDAO(con);
            UserRoleDAO userRoleDao = new UserRoleDAO(con);

            // 1. create organization
            Organization org = new Organization();
            org.setName(orgName);
            org.setEmail(orgEmail);
            org.setStatus("ACTIVE");
            org.setCreatedAt(LocalDateTime.now());

            long orgId = orgDao.insert(org);

            // 2. create org admin user
            User user = new User();
            user.setOrganizationId(orgId);
            user.setFullName(adminName);
            user.setEmail(adminEmail);
            user.setPasswordHash(passwordHash);
            user.setStatus("ACTIVE");
            user.setCreatedAt(LocalDateTime.now());

            long userId = userDao.insert(user);

            // 3. assign ORG_ADMIN role
            Role role = roleDao.findByName("ORG_ADMIN");

            userRoleDao.assignRole(userId, role.getId());

            con.commit();

        } catch (Exception ex) {

            if (con != null) con.rollback();
            throw ex;

        } finally {

            if (con != null) con.close();
        }
    }
}
