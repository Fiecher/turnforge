package com.github.fiecher.turnforge.config;

import com.github.fiecher.turnforge.config.containers.RepositoryContainer;
import com.github.fiecher.turnforge.config.containers.ServiceContainer;
import com.github.fiecher.turnforge.config.containers.UseCaseContainer;
import com.github.fiecher.turnforge.config.factories.CliCommandFactory;
import com.github.fiecher.turnforge.config.factories.PostgresRepositoryFactory;
import com.github.fiecher.turnforge.config.factories.ServiceFactory;
import com.github.fiecher.turnforge.config.factories.UseCaseFactory;
import com.github.fiecher.turnforge.domain.services.PasswordGenerator;
import com.github.fiecher.turnforge.infrastructure.db.postgres.PostgresConnectionManager;
import com.github.fiecher.turnforge.presentation.cli.ApplicationContext;
import com.github.fiecher.turnforge.presentation.cli.Menu;
import com.github.fiecher.turnforge.presentation.cli.input.InputReader;
import com.github.fiecher.turnforge.presentation.cli.output.View;

import java.sql.SQLException;

public class ApplicationConfigurator {

    private final ApplicationContext applicationContext;
    private final View view;
    private final InputReader reader;
    private PostgresConnectionManager connectionManager;

    public ApplicationConfigurator() {
        this.applicationContext = new ApplicationContext();
        this.view = new View();
        this.reader = new InputReader();
    }

    public Menu configureAndBuildMenu(String salt, String URL, String USER, String PASSWORD, String DRIVER) {
        PasswordGenerator.init(salt);
        connectionManager = PostgresConnectionManager.init(URL, USER, PASSWORD, DRIVER);

        CliCommandFactory commandFactory = getCliCommandFactory();

        Menu menu = new Menu(view, reader, applicationContext);

        menu.registerCommand(commandFactory.createCreateUserCommand(), true);
        menu.registerCommand(commandFactory.createLoginUserCommand(), true);
        menu.registerCommand(commandFactory.createCreateCharacterCommand(), false);
        menu.registerCommand(commandFactory.createGetCharactersCommand(), false);

        return menu;
    }

    private CliCommandFactory getCliCommandFactory() {
        PostgresRepositoryFactory repoFactory = new PostgresRepositoryFactory(connectionManager);
        RepositoryContainer repositories = repoFactory.createAllRepositories();

        ServiceFactory serviceFactory = new ServiceFactory(repositories);
        ServiceContainer services = serviceFactory.createAllServices();

        UseCaseFactory useCaseFactory = new UseCaseFactory(repositories, services);
        UseCaseContainer useCases = useCaseFactory.createAllUseCases();

        return new CliCommandFactory(useCases, applicationContext, view, reader);
    }

    public void closeConnection() throws SQLException {
        if (connectionManager != null) {
            connectionManager.getConnection().close();
        }
    }
}