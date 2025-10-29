package com.github.fiecher.turnforge.config.containers;

import com.github.fiecher.turnforge.app.usecase.*;

public class UseCaseContainer {
    private final CreateUserUseCase createUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final CreateCharacterUseCase createCharacterUseCase;
    private final GetCharactersUseCase getCharactersUseCase;

    public UseCaseContainer(CreateUserUseCase createUserUseCase, LoginUserUseCase loginUserUseCase, CreateCharacterUseCase createCharacterUseCase, GetCharactersUseCase getCharactersUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.createCharacterUseCase = createCharacterUseCase;
        this.getCharactersUseCase = getCharactersUseCase;
    }

    public CreateUserUseCase getCreateUserUseCase() {
        return createUserUseCase;
    }

    public LoginUserUseCase getLoginUserUseCase() {
        return loginUserUseCase;
    }

    public CreateCharacterUseCase getCreateCharacterUseCase() {
        return createCharacterUseCase;
    }

    public GetCharactersUseCase getGetCharactersUseCase() {
        return getCharactersUseCase;
    }
}