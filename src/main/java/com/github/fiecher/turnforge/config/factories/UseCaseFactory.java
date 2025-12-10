package com.github.fiecher.turnforge.config.factories;

import com.github.fiecher.turnforge.app.usecase.*;
import com.github.fiecher.turnforge.app.usecase.ability.CreateAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.DeleteAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.GetAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.UpdateAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.armor.CreateArmorUseCase;
import com.github.fiecher.turnforge.app.usecase.armor.DeleteArmorUseCase;
import com.github.fiecher.turnforge.app.usecase.armor.GetArmorUseCase;
import com.github.fiecher.turnforge.app.usecase.armor.UpdateArmorUseCase;
import com.github.fiecher.turnforge.app.usecase.character.AddAbilityToCharacterUseCase;
import com.github.fiecher.turnforge.app.usecase.character.CreateCharacterUseCase;
import com.github.fiecher.turnforge.app.usecase.character.GetCharactersUseCase;
import com.github.fiecher.turnforge.app.usecase.character.UpdateCharacterUseCase;
import com.github.fiecher.turnforge.app.usecase.item.CreateItemUseCase;
import com.github.fiecher.turnforge.app.usecase.item.DeleteItemUseCase;
import com.github.fiecher.turnforge.app.usecase.item.GetItemUseCase;
import com.github.fiecher.turnforge.app.usecase.item.UpdateItemUseCase;
import com.github.fiecher.turnforge.app.usecase.skill.CreateSkillUseCase;
import com.github.fiecher.turnforge.app.usecase.skill.DeleteSkillUseCase;
import com.github.fiecher.turnforge.app.usecase.skill.GetSkillUseCase;
import com.github.fiecher.turnforge.app.usecase.skill.UpdateSkillUseCase;
import com.github.fiecher.turnforge.app.usecase.trait.CreateTraitUseCase;
import com.github.fiecher.turnforge.app.usecase.trait.DeleteTraitUseCase;
import com.github.fiecher.turnforge.app.usecase.trait.GetTraitUseCase;
import com.github.fiecher.turnforge.app.usecase.trait.UpdateTraitUseCase;
import com.github.fiecher.turnforge.app.usecase.weapon.CreateWeaponUseCase;
import com.github.fiecher.turnforge.app.usecase.weapon.DeleteWeaponUseCase;
import com.github.fiecher.turnforge.app.usecase.weapon.GetWeaponUseCase;
import com.github.fiecher.turnforge.app.usecase.weapon.UpdateWeaponUseCase;
import com.github.fiecher.turnforge.config.containers.RepositoryContainer;
import com.github.fiecher.turnforge.config.containers.ServiceContainer;
import com.github.fiecher.turnforge.config.containers.UseCaseContainer;

import java.util.Objects;

public class UseCaseFactory {

    private final RepositoryContainer repositories;
    private final ServiceContainer services;

    public UseCaseFactory(RepositoryContainer repositories, ServiceContainer services) {
        this.repositories = Objects.requireNonNull(repositories);
        this.services = Objects.requireNonNull(services);
    }

    public CreateUserUseCase createCreateUserUseCase() {
        return new CreateUserUseCase(services.getUserService());
    }

    public LoginUserUseCase createLoginUserUseCase() {
        return new LoginUserUseCase(services.getUserService());
    }

    public CreateCharacterUseCase createCreateCharacterUseCase() {
        return new CreateCharacterUseCase(services.getCharacterService());
    }

    public GetCharactersUseCase createGetCharactersUseCase() {
        return new GetCharactersUseCase(
                repositories.getCharacterRepository(),
                repositories.getAbilityRepository(),
                repositories.getSkillRepository(),
                repositories.getWeaponRepository(),
                repositories.getArmorRepository(),
                repositories.getItemRepository(),
                repositories.getTraitRepository()
        );
    }

    public UpdateCharacterUseCase createUpdateCharacterStatsUseCase() {
        return new UpdateCharacterUseCase(services.getCharacterService());
    }

    public AddAbilityToCharacterUseCase createAddAbilityToCharacterUseCase() {
        return new AddAbilityToCharacterUseCase(services.getCharacterService());
    }

    public CreateAbilityUseCase createCreateAbilityUseCase() {
        return new CreateAbilityUseCase(services.getAbilityService());
    }
    public GetAbilityUseCase createGetAbilityUseCase() {
        return new GetAbilityUseCase(services.getAbilityService());
    }
    public UpdateAbilityUseCase createUpdateAbilityUseCase() {
        return new UpdateAbilityUseCase(services.getAbilityService());
    }
    public DeleteAbilityUseCase createDeleteAbilityUseCase() {
        return new DeleteAbilityUseCase(services.getAbilityService());
    }

    public CreateWeaponUseCase createCreateWeaponUseCase() {
        return new CreateWeaponUseCase(services.getWeaponService());
    }
    public GetWeaponUseCase createGetWeaponUseCase() {
        return new GetWeaponUseCase(services.getWeaponService());
    }
    public UpdateWeaponUseCase createUpdateWeaponUseCase() {
        return new UpdateWeaponUseCase(services.getWeaponService());
    }
    public DeleteWeaponUseCase createDeleteWeaponUseCase() {
        return new DeleteWeaponUseCase(services.getWeaponService());
    }

    public CreateArmorUseCase createCreateArmorUseCase() {
        return new CreateArmorUseCase(services.getArmorService());
    }
    public GetArmorUseCase createGetArmorUseCase() {
        return new GetArmorUseCase(services.getArmorService());
    }
    public UpdateArmorUseCase createUpdateArmorUseCase() {
        return new UpdateArmorUseCase(services.getArmorService());
    }
    public DeleteArmorUseCase createDeleteArmorUseCase() {
        return new DeleteArmorUseCase(services.getArmorService());
    }

    public CreateItemUseCase createCreateItemUseCase() {
        return new CreateItemUseCase(services.getItemService());
    }
    public GetItemUseCase createGetItemUseCase() {
        return new GetItemUseCase(services.getItemService());
    }
    public UpdateItemUseCase createUpdateItemUseCase() {
        return new UpdateItemUseCase(services.getItemService());
    }
    public DeleteItemUseCase createDeleteItemUseCase() {
        return new DeleteItemUseCase(services.getItemService());
    }

    public CreateSkillUseCase createCreateSkillUseCase() {
        return new CreateSkillUseCase(services.getSkillService());
    }
    public GetSkillUseCase createGetSkillUseCase() {
        return new GetSkillUseCase(services.getSkillService());
    }
    public UpdateSkillUseCase createUpdateSkillUseCase() {
        return new UpdateSkillUseCase(services.getSkillService());
    }
    public DeleteSkillUseCase createDeleteSkillUseCase() {
        return new DeleteSkillUseCase(services.getSkillService());
    }

    public CreateTraitUseCase createCreateTraitUseCase() {
        return new CreateTraitUseCase(services.getTraitService());
    }
    public GetTraitUseCase createGetTraitUseCase() {
        return new GetTraitUseCase(services.getTraitService());
    }
    public UpdateTraitUseCase createUpdateTraitUseCase() {
        return new UpdateTraitUseCase(services.getTraitService());
    }
    public DeleteTraitUseCase createDeleteTraitUseCase() {
        return new DeleteTraitUseCase(services.getTraitService());
    }


    public UseCaseContainer createAllUseCases() {
        return new UseCaseContainer(
                createCreateUserUseCase(),
                createLoginUserUseCase(),
                createCreateCharacterUseCase(),
                createGetCharactersUseCase(),
                createUpdateCharacterStatsUseCase(),
                createAddAbilityToCharacterUseCase(),

                createCreateAbilityUseCase(),
                createGetAbilityUseCase(),
                createUpdateAbilityUseCase(),
                createDeleteAbilityUseCase(),

                createCreateWeaponUseCase(),
                createGetWeaponUseCase(),
                createUpdateWeaponUseCase(),
                createDeleteWeaponUseCase(),

                createCreateArmorUseCase(),
                createGetArmorUseCase(),
                createUpdateArmorUseCase(),
                createDeleteArmorUseCase(),

                createCreateItemUseCase(),
                createGetItemUseCase(),
                createUpdateItemUseCase(),
                createDeleteItemUseCase(),

                createCreateSkillUseCase(),
                createGetSkillUseCase(),
                createUpdateSkillUseCase(),
                createDeleteSkillUseCase(),

                createCreateTraitUseCase(),
                createGetTraitUseCase(),
                createUpdateTraitUseCase(),
                createDeleteTraitUseCase()
        );
    }
}