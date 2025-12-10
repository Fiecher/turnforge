package com.github.fiecher.turnforge.config;

public record DatabaseConfig(String url, String user, String password, String driver) {

    public static DatabaseConfig readDatabaseConfig() {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        String driver = System.getenv("DB_DRIVER");

        if (url == null || url.isEmpty()) {
            throw new IllegalStateException("DB_URL environment variable is required and not set.");
        }
        if (user == null || user.isEmpty()) {
            throw new IllegalStateException("DB_USER environment variable is required and not set.");
        }
        if (password == null || password.isEmpty()) {
            System.out.println("[WARN] DB_PASSWORD is not set. Assuming an environment where password isn't needed.");
        }
        if (driver == null || driver.isEmpty()) {
            driver = "org.postgresql.Driver";
            System.out.println("[WARN] DB_DRIVER not set, defaulting to 'org.postgresql.Driver'.");
        }

        return new DatabaseConfig(url, user, password, driver);
    }

}
