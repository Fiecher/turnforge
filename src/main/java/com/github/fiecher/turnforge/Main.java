package com.github.fiecher.turnforge;

import com.github.fiecher.turnforge.config.ApplicationConfigurator;
import com.github.fiecher.turnforge.config.DatabaseConfig;
import com.github.fiecher.turnforge.domain.services.PasswordGenerator;
import com.github.fiecher.turnforge.presentation.cli.Menu;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && "web".equalsIgnoreCase(args[0])) {
            System.out.println("Starting in WEB mode. (Deployment via WAR file required)");
            System.out.println("To run in web mode, deploy as a WAR file to a servlet container or use an embedded server runner.");

        } else {
            System.out.println("Starting in CLI mode.");
            startCli();
        }
    }

    private static void startCli() {
        String salt = readSalt();
        DatabaseConfig dbConfig = DatabaseConfig.readDatabaseConfig();
        ApplicationConfigurator configurator = new ApplicationConfigurator();

        Menu menu = configurator.configureAndBuildMenu(
                salt,
                dbConfig.url(),
                dbConfig.user(),
                dbConfig.password(),
                dbConfig.driver()
        );

        menu.start();
    }

    private static String readSalt() {
        String salt = System.getenv("SALT");
        if (salt == null) {
            salt = PasswordGenerator.generateSalt(18);
            System.out.println("[WARN] SALT not set, using generated default.");
        }
        return salt;
    }

}