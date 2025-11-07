package com.github.fiecher.turnforge.config;

import com.github.fiecher.turnforge.config.containers.RepositoryContainer;
import com.github.fiecher.turnforge.config.containers.ServiceContainer;
import com.github.fiecher.turnforge.config.containers.UseCaseContainer;
import com.github.fiecher.turnforge.config.factories.InMemoryRepositoryFactory;
import com.github.fiecher.turnforge.config.factories.ServiceFactory;
import com.github.fiecher.turnforge.config.factories.UseCaseFactory;
import com.github.fiecher.turnforge.domain.services.PasswordGenerator;
import com.github.fiecher.turnforge.presentation.cli.ApplicationContext;
import com.github.fiecher.turnforge.config.factories.CliCommandFactory;
import com.github.fiecher.turnforge.presentation.cli.Menu;
import com.github.fiecher.turnforge.presentation.cli.output.View;
import com.github.fiecher.turnforge.presentation.cli.input.InputReader;

public class ApplicationConfigurator {

    private final ApplicationContext applicationContext;
    private final View view;
    private final InputReader reader;

    public ApplicationConfigurator() {
        this.applicationContext = new ApplicationContext();
        this.view = new View();
        this.reader = new InputReader();
    }

    public Menu configureAndBuildMenu(String salt) {
        PasswordGenerator.init(salt);

        CliCommandFactory commandFactory = getCliCommandFactory();

        Menu menu = new Menu(view, reader, applicationContext);

        menu.registerCommand(commandFactory.createCreateUserCommand(), true);
        menu.registerCommand(commandFactory.createLoginUserCommand(), true);
        menu.registerCommand(commandFactory.createCreateCharacterCommand(), false);
        menu.registerCommand(commandFactory.createGetCharactersCommand(), false);

        return menu;
    }

    private CliCommandFactory getCliCommandFactory() {
        InMemoryRepositoryFactory repoFactory = new InMemoryRepositoryFactory();
        RepositoryContainer repositories = repoFactory.createAllRepositories();

        ServiceFactory serviceFactory = new ServiceFactory(repositories);
        ServiceContainer services = serviceFactory.createAllServices();

        UseCaseFactory useCaseFactory = new UseCaseFactory(repositories, services);
        UseCaseContainer useCases = useCaseFactory.createAllUseCases();

        return new CliCommandFactory(useCases, applicationContext, view, reader);
    }
}