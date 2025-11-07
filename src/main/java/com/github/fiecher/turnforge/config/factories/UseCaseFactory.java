package com.github.fiecher.turnforge.config.factories;

import com.github.fiecher.turnforge.app.usecase.*;
import com.github.fiecher.turnforge.config.containers.RepositoryContainer;
import com.github.fiecher.turnforge.config.containers.ServiceContainer;
import com.github.fiecher.turnforge.config.containers.UseCaseContainer;
import com.github.fiecher.turnforge.domain.services.CharacterService;
import com.github.fiecher.turnforge.domain.services.UserService;

public class UseCaseFactory {

    private final RepositoryContainer repositories;
    private final ServiceContainer services;

    public UseCaseFactory(RepositoryContainer repositories, ServiceContainer services) {
        this.repositories = repositories;
        this.services = services;
    }

    public CreateUserUseCase createCreateUserUseCase() {
        UserService userService = services.getUserService();
        return new CreateUserUseCase(userService);
    }

    public LoginUserUseCase createLoginUserUseCase() {
        UserService userService = services.getUserService();
        return new LoginUserUseCase(userService);
    }

    public CreateCharacterUseCase createCreateCharacterUseCase() {
        CharacterService characterService = services.getCharacterService();
        return new CreateCharacterUseCase(characterService);
    }

    public GetCharactersUseCase createGetCharactersUseCase() {
        return new GetCharactersUseCase(repositories.getCharacterRepository(), repositories.getAbilityRepository(), repositories.getSkillRepository(), repositories.getWeaponRepository(), repositories.getArmorRepository(), repositories.getItemRepository());
    }

    public UseCaseContainer createAllUseCases() {
        return new UseCaseContainer(createCreateUserUseCase(), createLoginUserUseCase(), createCreateCharacterUseCase(), createGetCharactersUseCase());
    }
}