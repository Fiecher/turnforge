package com.github.fiecher.turnforge.config.factories;

import com.github.fiecher.turnforge.config.containers.ServletContainer;
import com.github.fiecher.turnforge.config.containers.UseCaseContainer;
import com.github.fiecher.turnforge.infrastructure.security.TokenService;
import com.github.fiecher.turnforge.presentation.servlet.api.*;
import java.util.Objects;

public class ServletFactory {

    private final UseCaseContainer useCases;
    private final TokenService tokenService;

    public ServletFactory(UseCaseContainer useCases, TokenService tokenService) {
        this.useCases = Objects.requireNonNull(useCases, "UseCaseContainer must not be null");
        this.tokenService = Objects.requireNonNull(tokenService, "TokenService must not be null");
    }

    public ServletContainer createAllServlets() {
        System.out.println("-> Injecting UseCases into Servlets...");

        UserServlet.setDependencies(
                useCases.getCreateUserUseCase(),
                useCases.getLoginUserUseCase(),
                tokenService
        );
        UserServlet userServlet = new UserServlet();

        CharacterServlet characterServlet = new CharacterServlet(
                useCases.getCreateCharacterUseCase(),
                useCases.getGetCharactersUseCase(),
                useCases.getUpdateCharacterStatsUseCase(),
                useCases.getAddAbilityToCharacterUseCase()
        );

        AbilityServlet.setDependencies(
                useCases.getCreateAbilityUseCase(), useCases.getGetAbilityUseCase(),
                useCases.getUpdateAbilityUseCase(), useCases.getDeleteAbilityUseCase()
        );
        AbilityServlet abilityServlet = new AbilityServlet();

        WeaponServlet.setDependencies(
                useCases.getCreateWeaponUseCase(), useCases.getGetWeaponUseCase(),
                useCases.getUpdateWeaponUseCase(), useCases.getDeleteWeaponUseCase()
        );
        WeaponServlet weaponServlet = new WeaponServlet();

        ArmorServlet.setDependencies(
                useCases.getCreateArmorUseCase(), useCases.getGetArmorUseCase(),
                useCases.getUpdateArmorUseCase(), useCases.getDeleteArmorUseCase()
        );
        ArmorServlet armorServlet = new ArmorServlet();

        ItemServlet.setDependencies(
                useCases.getCreateItemUseCase(), useCases.getGetItemUseCase(),
                useCases.getUpdateItemUseCase(), useCases.getDeleteItemUseCase()
        );
        ItemServlet itemServlet = new ItemServlet();

        SkillServlet.setDependencies(
                useCases.getCreateSkillUseCase(), useCases.getGetSkillUseCase(),
                useCases.getUpdateSkillUseCase(), useCases.getDeleteSkillUseCase()
        );
        SkillServlet skillServlet = new SkillServlet();

        TraitServlet.setDependencies(
                useCases.getCreateTraitUseCase(), useCases.getGetTraitUseCase(),
                useCases.getUpdateTraitUseCase(), useCases.getDeleteTraitUseCase()
        );
        TraitServlet traitServlet = new TraitServlet();


        return new ServletContainer(userServlet, characterServlet, abilityServlet, weaponServlet, armorServlet, itemServlet, skillServlet, traitServlet);
    }
}