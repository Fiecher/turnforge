package com.github.fiecher.turnforge.infrastructure.db.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnectionManager {

    private static PostgresConnectionManager INSTANCE;
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String DRIVER;
    private final ThreadLocal<Connection> threadConnection = new ThreadLocal<>();

    private PostgresConnectionManager(String URL, String USER, String PASSWORD, String DRIVER) {
        PostgresConnectionManager.URL = URL;
        PostgresConnectionManager.USER = USER;
        PostgresConnectionManager.PASSWORD = PASSWORD;
        PostgresConnectionManager.DRIVER = DRIVER;
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("PostgreSQL JDBC Driver not found: " + DRIVER);
        }
    }

    public static PostgresConnectionManager init(String URL, String USER, String PASSWORD, String DRIVER) {
        if (INSTANCE != null) {
            throw new IllegalStateException("PostgresConnectionManager is already initialized");
        }

        INSTANCE = new PostgresConnectionManager(URL, USER, PASSWORD, DRIVER);
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        if (INSTANCE == null) {
            throw new IllegalStateException("PostgresConnectionManager not initialized. Call init() first.");
        }

        Connection conn = threadConnection.get();
        if (conn != null) {
            return conn;
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void beginTransaction() throws SQLException {
        if (threadConnection.get() != null) {
            throw new IllegalStateException("Transaction already active in this thread.");
        }

        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false);
        threadConnection.set(conn);
    }

    public void commit() throws SQLException {
        Connection conn = threadConnection.get();
        if (conn == null) {
            throw new IllegalStateException("No active transaction to commit.");
        }
        try {
            conn.commit();
        } finally {
            closeConnection(conn);
        }
    }

    public void rollback() throws SQLException {
        Connection conn = threadConnection.get();
        if (conn == null) {
            return;
        }
        try {
            conn.rollback();
        } finally {
            closeConnection(conn);
        }
    }

    private void closeConnection(Connection conn) {
        try {
            conn.setAutoCommit(true);
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        } finally {
            threadConnection.remove();
        }
    }
}