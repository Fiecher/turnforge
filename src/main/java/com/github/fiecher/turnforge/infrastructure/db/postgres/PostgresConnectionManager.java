package com.github.fiecher.turnforge.infrastructure.db.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class PostgresConnectionManager {

    private static PostgresConnectionManager INSTANCE;
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String DRIVER;

    private PostgresConnectionManager(String URL, String USER, String PASSWORD, String DRIVER) {
        PostgresConnectionManager.URL = URL;
        PostgresConnectionManager.USER = USER;
        PostgresConnectionManager.PASSWORD = PASSWORD;
        PostgresConnectionManager.DRIVER = DRIVER;
    }

    public static PostgresConnectionManager init(String URL, String USER, String PASSWORD, String DRIVER) {
        Objects.requireNonNull(URL, "URL cannot be null");
        Objects.requireNonNull(USER, "USER cannot be null");
        Objects.requireNonNull(PASSWORD, "PASSWORD cannot be null");
        Objects.requireNonNull(DRIVER, "DRIVER cannot be null");

        if (INSTANCE != null) {
            throw new IllegalStateException("PostgresConnectionManager is already initialized");
        }

        PostgresConnectionManager.URL = URL;
        PostgresConnectionManager.USER = USER;
        PostgresConnectionManager.PASSWORD = PASSWORD;
        PostgresConnectionManager.DRIVER = DRIVER;

        INSTANCE = new PostgresConnectionManager(URL, USER, PASSWORD, DRIVER);

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("PostgreSQL JDBC Driver not found: " + DRIVER);
        }

        return INSTANCE;
    }

    public static Connection getConnection() throws SQLException {
        if (INSTANCE == null) {
            throw new IllegalStateException("PostgresConnectionManager not initialized. Call init() first.");
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable res : resources) {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    System.err.println("Error closing resources: " + e.getMessage());
                }
            }
        }
    }
}