package com.github.fiecher.turnforge.config.containers;

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

import java.util.Objects;

public class UseCaseContainer {
    private final CreateUserUseCase createUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final CreateCharacterUseCase createCharacterUseCase;
    private final GetCharactersUseCase getCharactersUseCase;
    private final UpdateCharacterUseCase updateCharacterStatsUseCase;
    private final AddAbilityToCharacterUseCase addAbilityToCharacterUseCase;

    private final CreateAbilityUseCase createAbilityUseCase;
    private final GetAbilityUseCase getAbilityUseCase;
    private final UpdateAbilityUseCase updateAbilityUseCase;
    private final DeleteAbilityUseCase deleteAbilityUseCase;

    private final CreateWeaponUseCase createWeaponUseCase;
    private final GetWeaponUseCase getWeaponUseCase;
    private final UpdateWeaponUseCase updateWeaponUseCase;
    private final DeleteWeaponUseCase deleteWeaponUseCase;

    private final CreateArmorUseCase createArmorUseCase;
    private final GetArmorUseCase getArmorUseCase;
    private final UpdateArmorUseCase updateArmorUseCase;
    private final DeleteArmorUseCase deleteArmorUseCase;

    private final CreateItemUseCase createItemUseCase;
    private final GetItemUseCase getItemUseCase;
    private final UpdateItemUseCase updateItemUseCase;
    private final DeleteItemUseCase deleteItemUseCase;

    private final CreateSkillUseCase createSkillUseCase;
    private final GetSkillUseCase getSkillUseCase;
    private final UpdateSkillUseCase updateSkillUseCase;
    private final DeleteSkillUseCase deleteSkillUseCase;

    private final CreateTraitUseCase createTraitUseCase;
    private final GetTraitUseCase getTraitUseCase;
    private final UpdateTraitUseCase updateTraitUseCase;
    private final DeleteTraitUseCase deleteTraitUseCase;

    public UseCaseContainer(
            CreateUserUseCase createUserUseCase, LoginUserUseCase loginUserUseCase,
            CreateCharacterUseCase createCharacterUseCase, GetCharactersUseCase getCharactersUseCase,
            UpdateCharacterUseCase updateCharacterStatsUseCase, AddAbilityToCharacterUseCase addAbilityToCharacterUseCase,

            CreateAbilityUseCase createAbilityUseCase, GetAbilityUseCase getAbilityUseCase, UpdateAbilityUseCase updateAbilityUseCase, DeleteAbilityUseCase deleteAbilityUseCase,
            CreateWeaponUseCase createWeaponUseCase, GetWeaponUseCase getWeaponUseCase, UpdateWeaponUseCase updateWeaponUseCase, DeleteWeaponUseCase deleteWeaponUseCase,
            CreateArmorUseCase createArmorUseCase, GetArmorUseCase getArmorUseCase, UpdateArmorUseCase updateArmorUseCase, DeleteArmorUseCase deleteArmorUseCase,
            CreateItemUseCase createItemUseCase, GetItemUseCase getItemUseCase, UpdateItemUseCase updateItemUseCase, DeleteItemUseCase deleteItemUseCase,
            CreateSkillUseCase createSkillUseCase, GetSkillUseCase getSkillUseCase, UpdateSkillUseCase updateSkillUseCase, DeleteSkillUseCase deleteSkillUseCase,
            CreateTraitUseCase createTraitUseCase, GetTraitUseCase getTraitUseCase, UpdateTraitUseCase updateTraitUseCase, DeleteTraitUseCase deleteTraitUseCase)
    {
        this.createUserUseCase = Objects.requireNonNull(createUserUseCase);
        this.loginUserUseCase = Objects.requireNonNull(loginUserUseCase);
        this.createCharacterUseCase = Objects.requireNonNull(createCharacterUseCase);
        this.getCharactersUseCase = Objects.requireNonNull(getCharactersUseCase);
        this.updateCharacterStatsUseCase = Objects.requireNonNull(updateCharacterStatsUseCase);
        this.addAbilityToCharacterUseCase = Objects.requireNonNull(addAbilityToCharacterUseCase);

        this.createAbilityUseCase = Objects.requireNonNull(createAbilityUseCase);
        this.getAbilityUseCase = Objects.requireNonNull(getAbilityUseCase);
        this.updateAbilityUseCase = Objects.requireNonNull(updateAbilityUseCase);
        this.deleteAbilityUseCase = Objects.requireNonNull(deleteAbilityUseCase);

        this.createWeaponUseCase = Objects.requireNonNull(createWeaponUseCase);
        this.getWeaponUseCase = Objects.requireNonNull(getWeaponUseCase);
        this.updateWeaponUseCase = Objects.requireNonNull(updateWeaponUseCase);
        this.deleteWeaponUseCase = Objects.requireNonNull(deleteWeaponUseCase);

        this.createArmorUseCase = Objects.requireNonNull(createArmorUseCase);
        this.getArmorUseCase = Objects.requireNonNull(getArmorUseCase);
        this.updateArmorUseCase = Objects.requireNonNull(updateArmorUseCase);
        this.deleteArmorUseCase = Objects.requireNonNull(deleteArmorUseCase);

        this.createItemUseCase = Objects.requireNonNull(createItemUseCase);
        this.getItemUseCase = Objects.requireNonNull(getItemUseCase);
        this.updateItemUseCase = Objects.requireNonNull(updateItemUseCase);
        this.deleteItemUseCase = Objects.requireNonNull(deleteItemUseCase);

        this.createSkillUseCase = Objects.requireNonNull(createSkillUseCase);
        this.getSkillUseCase = Objects.requireNonNull(getSkillUseCase);
        this.updateSkillUseCase = Objects.requireNonNull(updateSkillUseCase);
        this.deleteSkillUseCase = Objects.requireNonNull(deleteSkillUseCase);

        this.createTraitUseCase = Objects.requireNonNull(createTraitUseCase);
        this.getTraitUseCase = Objects.requireNonNull(getTraitUseCase);
        this.updateTraitUseCase = Objects.requireNonNull(updateTraitUseCase);
        this.deleteTraitUseCase = Objects.requireNonNull(deleteTraitUseCase);
    }

    public CreateUserUseCase getCreateUserUseCase() { return createUserUseCase; }
    public LoginUserUseCase getLoginUserUseCase() { return loginUserUseCase; }
    public CreateCharacterUseCase getCreateCharacterUseCase() { return createCharacterUseCase; }
    public GetCharactersUseCase getGetCharactersUseCase() { return getCharactersUseCase; }
    public UpdateCharacterUseCase getUpdateCharacterStatsUseCase() { return updateCharacterStatsUseCase; }
    public AddAbilityToCharacterUseCase getAddAbilityToCharacterUseCase() { return addAbilityToCharacterUseCase; }

    public CreateAbilityUseCase getCreateAbilityUseCase() { return createAbilityUseCase; }
    public GetAbilityUseCase getGetAbilityUseCase() { return getAbilityUseCase; }
    public UpdateAbilityUseCase getUpdateAbilityUseCase() { return updateAbilityUseCase; }
    public DeleteAbilityUseCase getDeleteAbilityUseCase() { return deleteAbilityUseCase; }

    public CreateWeaponUseCase getCreateWeaponUseCase() { return createWeaponUseCase; }
    public GetWeaponUseCase getGetWeaponUseCase() { return getWeaponUseCase; }
    public UpdateWeaponUseCase getUpdateWeaponUseCase() { return updateWeaponUseCase; }
    public DeleteWeaponUseCase getDeleteWeaponUseCase() { return deleteWeaponUseCase; }

    public CreateArmorUseCase getCreateArmorUseCase() { return createArmorUseCase; }
    public GetArmorUseCase getGetArmorUseCase() { return getArmorUseCase; }
    public UpdateArmorUseCase getUpdateArmorUseCase() { return updateArmorUseCase; }
    public DeleteArmorUseCase getDeleteArmorUseCase() { return deleteArmorUseCase; }

    public CreateItemUseCase getCreateItemUseCase() { return createItemUseCase; }
    public GetItemUseCase getGetItemUseCase() { return getItemUseCase; }
    public UpdateItemUseCase getUpdateItemUseCase() { return updateItemUseCase; }
    public DeleteItemUseCase getDeleteItemUseCase() { return deleteItemUseCase; }

    public CreateSkillUseCase getCreateSkillUseCase() { return createSkillUseCase; }
    public GetSkillUseCase getGetSkillUseCase() { return getSkillUseCase; }
    public UpdateSkillUseCase getUpdateSkillUseCase() { return updateSkillUseCase; }
    public DeleteSkillUseCase getDeleteSkillUseCase() { return deleteSkillUseCase; }

    public CreateTraitUseCase getCreateTraitUseCase() { return createTraitUseCase; }
    public GetTraitUseCase getGetTraitUseCase() { return getTraitUseCase; }
    public UpdateTraitUseCase getUpdateTraitUseCase() { return updateTraitUseCase; }
    public DeleteTraitUseCase getDeleteTraitUseCase() { return deleteTraitUseCase; }
}