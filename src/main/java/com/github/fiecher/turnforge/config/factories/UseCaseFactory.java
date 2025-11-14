package com.github.fiecher.turnforge.config.factories;

import com.github.fiecher.turnforge.app.usecase.CreateCharacterUseCase;
import com.github.fiecher.turnforge.app.usecase.CreateUserUseCase;
import com.github.fiecher.turnforge.app.usecase.GetCharactersUseCase;
import com.github.fiecher.turnforge.app.usecase.LoginUserUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.CreateAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.DeleteAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.GetAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.UpdateAbilityUseCase;
import com.github.fiecher.turnforge.config.containers.RepositoryContainer;
import com.github.fiecher.turnforge.config.containers.ServiceContainer;
import com.github.fiecher.turnforge.config.containers.UseCaseContainer;
import com.github.fiecher.turnforge.domain.services.AbilityService;
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
        UserService userService = services.userService();
        return new CreateUserUseCase(userService);
    }

    public LoginUserUseCase createLoginUserUseCase() {
        UserService userService = services.userService();
        return new LoginUserUseCase(userService);
    }

    public CreateCharacterUseCase createCreateCharacterUseCase() {
        CharacterService characterService = services.characterService();
        return new CreateCharacterUseCase(characterService);
    }

    public GetCharactersUseCase createGetCharactersUseCase() {
        return new GetCharactersUseCase(repositories.characterRepository(), repositories.abilityRepository(), repositories.skillRepository(), repositories.weaponRepository(), repositories.armorRepository(), repositories.itemRepository());
    }

    public CreateAbilityUseCase createCreateAbilityUseCase() {
        AbilityService abilityService = services.abilityService();
        return new CreateAbilityUseCase(abilityService);
    }

    public GetAbilityUseCase createGetAbilityUseCase() {
        AbilityService abilityService = services.abilityService();
        return new GetAbilityUseCase(abilityService);
    }

    public UpdateAbilityUseCase createUpdateAbilityUseCase() {
        AbilityService abilityService = services.abilityService();
        return new UpdateAbilityUseCase(abilityService);
    }

    public DeleteAbilityUseCase createDeleteAbilityUseCase() {
        AbilityService abilityService = services.abilityService();
        return new DeleteAbilityUseCase(abilityService);
    }


    public UseCaseContainer createAllUseCases() {
        return new UseCaseContainer(
                createCreateUserUseCase(),
                createLoginUserUseCase(),
                createCreateCharacterUseCase(),
                createGetCharactersUseCase(),
                createCreateAbilityUseCase(),
                createGetAbilityUseCase(),
                createUpdateAbilityUseCase(),
                createDeleteAbilityUseCase()
        );
    }
}