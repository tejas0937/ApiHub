package com.example.demo.util;

import java.sql.Connection;

public class DBCheck {

    public static void main(String[] args) {

        try (Connection con = DBUtil.getConnection()) {

            System.out.println("DBUtil connection successful");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
