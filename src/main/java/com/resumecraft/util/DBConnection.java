package com.resumecraft.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility.
 * Reads connection parameters from environment variables for cloud-safe configuration.
 */
public class DBConnection {

    private static final String DEFAULT_URL  = "jdbc:mysql://localhost:3306/resumecraft_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Returns a new database connection.
     * Uses environment variables if available, otherwise falls back to defaults.
     *
     * Environment variables (set on Railway / Docker):
     *   MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD
     */
    public static Connection getConnection() throws SQLException {
        String url  = System.getenv("MYSQL_URL")      != null ? System.getenv("MYSQL_URL")      : DEFAULT_URL;
        String user = System.getenv("MYSQL_USER")     != null ? System.getenv("MYSQL_USER")     : DEFAULT_USER;
        String pass = System.getenv("MYSQL_PASSWORD") != null ? System.getenv("MYSQL_PASSWORD") : DEFAULT_PASS;
        return DriverManager.getConnection(url, user, pass);
    }
}
