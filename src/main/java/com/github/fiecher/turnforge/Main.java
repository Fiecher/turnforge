package com.github.fiecher.turnforge;

import com.github.fiecher.turnforge.config.ApplicationConfigurator;
import com.github.fiecher.turnforge.domain.services.PasswordGenerator;
import com.github.fiecher.turnforge.presentation.cli.Menu;

public class Main {

    private static class DatabaseConfig {
        final String url;
        final String user;
        final String password;
        final String driver;

        DatabaseConfig(String url, String user, String password, String driver) {
            this.url = url;
            this.user = user;
            this.password = password;
            this.driver = driver;
        }
    }

    public static void main(String[] args) {
        String salt = readSalt();

        DatabaseConfig dbConfig = readDatabaseConfig();

        ApplicationConfigurator configurator = new ApplicationConfigurator();

        Menu menu = configurator.configureAndBuildMenu(
                salt,
                dbConfig.url,
                dbConfig.user,
                dbConfig.password,
                dbConfig.driver
        );

        menu.start();
    }

    private static String readSalt() {
        String salt = System.getenv("SALT");
        if (salt == null){
            salt = PasswordGenerator.generateSalt(18);
            System.out.println("[WARN] SALT not set, using generated default.");
        }
        return salt;
    }

    private static DatabaseConfig readDatabaseConfig() {
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