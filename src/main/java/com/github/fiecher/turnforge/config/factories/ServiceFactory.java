package com.github.fiecher.turnforge.config.factories;


import com.github.fiecher.turnforge.config.containers.RepositoryContainer;
import com.github.fiecher.turnforge.config.containers.ServiceContainer;
import com.github.fiecher.turnforge.domain.services.*;

import java.util.Objects;

public class ServiceFactory {

    private final RepositoryContainer repositories;

    public ServiceFactory(RepositoryContainer repositories) {
        this.repositories = Objects.requireNonNull(repositories, "RepositoryContainer must not be null");
    }

    public UserService createUserService() {
        return new UserService(repositories.getUserRepository());
    }


    public CharacterService createCharacterService() {
        return new CharacterService(
                repositories.getCharacterRepository(),
                repositories.getAbilityRepository(),
                repositories.getWeaponRepository(),
                repositories.getArmorRepository(),
                repositories.getItemRepository(),
                repositories.getTraitRepository(),
                repositories.getSkillRepository()
        );
    }

    public AbilityService createAbilityService() {
        return new AbilityService(repositories.getAbilityRepository());
    }

    public WeaponService createWeaponService() {
        return new WeaponService(repositories.getWeaponRepository());
    }

    public ArmorService createArmorService() {
        return new ArmorService(repositories.getArmorRepository());
    }

    public ItemService createItemService() {
        return new ItemService(repositories.getItemRepository());
    }

    public SkillService createSkillService() {
        return new SkillService(repositories.getSkillRepository());
    }

    public TraitService createTraitService() {
        return new TraitService(repositories.getTraitRepository());
    }

    public ServiceContainer createAllServices() {
        return new ServiceContainer(
                createCharacterService(),
                createUserService(),
                createAbilityService(),
                createWeaponService(),
                createArmorService(),
                createItemService(),
                createSkillService(),
                createTraitService()
        );
    }
}