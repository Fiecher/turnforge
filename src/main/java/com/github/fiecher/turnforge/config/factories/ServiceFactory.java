package com.github.fiecher.turnforge.config.factories;

import com.github.fiecher.turnforge.config.containers.RepositoryContainer;
import com.github.fiecher.turnforge.config.containers.ServiceContainer;
import com.github.fiecher.turnforge.domain.repositories.AbilityRepository;
import com.github.fiecher.turnforge.domain.services.AbilityService;
import com.github.fiecher.turnforge.domain.services.CharacterService;
import com.github.fiecher.turnforge.domain.services.UserService;
import java.util.Objects;


public class ServiceFactory {

    private final RepositoryContainer repositories;

    public ServiceFactory(RepositoryContainer repositories) {
        this.repositories = Objects.requireNonNull(repositories);
    }

    public UserService createUserService() {
        return new UserService(repositories.userRepository());
    }

    public CharacterService createCharacterService() {
        return new CharacterService(repositories.characterRepository(), repositories.abilityRepository(), repositories.weaponRepository(), repositories.armorRepository(), repositories.itemRepository(), repositories.traitRepository(), repositories.skillRepository());
    }

    public AbilityService createAbilityService() {
        AbilityRepository abilityRepository = repositories.abilityRepository();
        return new AbilityService(abilityRepository);
    }

    public ServiceContainer createAllServices() {
        return new ServiceContainer(
                createCharacterService(),
                createUserService(),
                createAbilityService()
        );
    }
}