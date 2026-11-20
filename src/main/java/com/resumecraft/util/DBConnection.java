package com.resumecraft.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility.
 * Reads connection parameters from environment variables for cloud-safe configuration (e.g. Railway / Docker),
 * with full fallback support for local development.
 *
 * Supported environment variables:
 *   - MYSQL_URL or DATABASE_URL
 *   - MYSQLHOST, MYSQLPORT, MYSQLDATABASE
 *   - MYSQLUSER or MYSQL_USER
 *   - MYSQLPASSWORD or MYSQL_PASSWORD
 */
public class DBConnection {

    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_DB   = "resumecraft_db";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "";
    private static final String DEFAULT_URL  = "jdbc:mysql://" + DEFAULT_HOST + ":" + DEFAULT_PORT + "/" + DEFAULT_DB
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Returns a new database connection.
     * Checks cloud environment variables (Railway / Docker / standard env vars)
     * and falls back to local development defaults.
     */
    public static Connection getConnection() throws SQLException {
        String rawUrl = getEnv("MYSQL_URL", getEnv("DATABASE_URL", null));
        String user   = getEnv("MYSQLUSER", getEnv("MYSQL_USER", null));
        String pass   = getEnv("MYSQLPASSWORD", getEnv("MYSQL_PASSWORD", null));

        String jdbcUrl;

        if (rawUrl != null && !rawUrl.trim().isEmpty()) {
            // Case 1: MYSQL_URL / DATABASE_URL is provided
            rawUrl = rawUrl.trim();

            // Extract embedded credentials if URL has user:pass@ format (e.g. mysql://user:pass@host:port/db)
            if (rawUrl.contains("@")) {
                int schemeEnd = rawUrl.indexOf("://");
                String scheme = schemeEnd != -1 ? rawUrl.substring(0, schemeEnd + 3) : "";
                String remainder = schemeEnd != -1 ? rawUrl.substring(schemeEnd + 3) : rawUrl;

                int atIndex = remainder.indexOf("@");
                String userInfo = remainder.substring(0, atIndex);
                String hostAndRest = remainder.substring(atIndex + 1);

                if (user == null && userInfo.contains(":")) {
                    user = userInfo.substring(0, userInfo.indexOf(":"));
                    if (pass == null) {
                        pass = userInfo.substring(userInfo.indexOf(":") + 1);
                    }
                } else if (user == null) {
                    user = userInfo;
                }

                rawUrl = scheme + hostAndRest;
            }

            // Convert mysql:// or mariadb:// to jdbc:mysql://
            if (rawUrl.startsWith("mysql://")) {
                jdbcUrl = "jdbc:" + rawUrl;
            } else if (rawUrl.startsWith("jdbc:mysql://")) {
                jdbcUrl = rawUrl;
            } else {
                jdbcUrl = "jdbc:mysql://" + rawUrl;
            }

            // Ensure recommended connection parameters if not present
            if (!jdbcUrl.contains("allowPublicKeyRetrieval")) {
                jdbcUrl += (jdbcUrl.contains("?") ? "&" : "?") + "allowPublicKeyRetrieval=true";
            }
            if (!jdbcUrl.contains("serverTimezone")) {
                jdbcUrl += (jdbcUrl.contains("?") ? "&" : "?") + "serverTimezone=UTC";
            }
        } else {
            // Case 2: Individual variables (MYSQLHOST, MYSQLPORT, MYSQLDATABASE)
            String host = getEnv("MYSQLHOST", null);
            if (host != null && !host.trim().isEmpty()) {
                String port = getEnv("MYSQLPORT", DEFAULT_PORT);
                String db   = getEnv("MYSQLDATABASE", DEFAULT_DB);
                jdbcUrl = "jdbc:mysql://" + host.trim() + ":" + port.trim() + "/" + db.trim()
                        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            } else {
                // Case 3: Fallback for local development
                jdbcUrl = DEFAULT_URL;
            }
        }

        // Apply fallback credentials for local dev if not supplied by env
        if (user == null) {
            user = DEFAULT_USER;
        }
        if (pass == null) {
            pass = DEFAULT_PASS;
        }

        return DriverManager.getConnection(jdbcUrl, user, pass);
    }

    private static String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.trim().isEmpty()) ? value : defaultValue;
    }
}
