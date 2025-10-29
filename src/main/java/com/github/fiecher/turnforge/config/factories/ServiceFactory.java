package com.github.fiecher.turnforge.config.factories;


import com.github.fiecher.turnforge.config.containers.RepositoryContainer;
import com.github.fiecher.turnforge.config.containers.ServiceContainer;
import com.github.fiecher.turnforge.domain.services.CharacterService;
import com.github.fiecher.turnforge.domain.services.UserService;

public class ServiceFactory {

    private final RepositoryContainer repositories;

    public ServiceFactory(RepositoryContainer repositories) {
        this.repositories = repositories;
    }

    public CharacterService createCharacterService() {
        return new CharacterService(repositories.getCharacterRepository(), repositories.getAbilityRepository(), repositories.getWeaponRepository(), repositories.getArmorRepository(), repositories.getItemRepository(), repositories.getTraitRepository(), repositories.getSkillRepository());
    }

    public UserService createUserService() {
        return new UserService(repositories.getUserRepository());
    }

    public ServiceContainer createAllServices() {
        return new ServiceContainer(createCharacterService(), createUserService());
    }
}