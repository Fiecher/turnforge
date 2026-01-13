package com.github.fiecher.turnforge.config.factories;

import com.github.fiecher.turnforge.config.containers.UseCaseContainer;
import com.github.fiecher.turnforge.presentation.cli.ApplicationContext;
import com.github.fiecher.turnforge.presentation.cli.commands.character.CreateCharacterCommand;
import com.github.fiecher.turnforge.presentation.cli.commands.character.GetCharactersCommand;
import com.github.fiecher.turnforge.presentation.cli.commands.user.CreateUserCommand;
import com.github.fiecher.turnforge.presentation.cli.commands.user.LoginUserCommand;
import com.github.fiecher.turnforge.presentation.cli.input.InputReader;
import com.github.fiecher.turnforge.presentation.cli.output.View;

public class CliCommandFactory {
    private final UseCaseContainer useCases;
    private final ApplicationContext applicationContext;
    private final View view;
    private final InputReader reader;

    public CliCommandFactory(UseCaseContainer useCases, ApplicationContext applicationContext, View view, InputReader reader) {
        this.useCases = useCases;
        this.applicationContext = applicationContext;
        this.view = view;
        this.reader = reader;
    }

    public CreateUserCommand createCreateUserCommand() {
        return new CreateUserCommand(useCases.createUserUseCase(), view, reader);
    }

    public LoginUserCommand createLoginUserCommand() {
        return new LoginUserCommand(useCases.loginUserUseCase(), applicationContext, view, reader);
    }

    public CreateCharacterCommand createCreateCharacterCommand() {
        return new CreateCharacterCommand(useCases.createCharacterUseCase(), applicationContext, view, reader);
    }

    public GetCharactersCommand createGetCharactersCommand() {
        return new GetCharactersCommand(useCases.getCharactersUseCase(), applicationContext, view);
    }
}