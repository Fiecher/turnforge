package com.github.fiecher.turnforge.config.containers;

import com.github.fiecher.turnforge.domain.services.*;
import java.util.Objects;

public class ServiceContainer {
    private final CharacterService characterService;
    private final UserService userService;
    private final AbilityService abilityService;
    private final WeaponService weaponService;
    private final ArmorService armorService;
    private final ItemService itemService;
    private final SkillService skillService;
    private final TraitService traitService;

    public ServiceContainer(
            CharacterService characterService,
            UserService userService,
            AbilityService abilityService,
            WeaponService weaponService,
            ArmorService armorService,
            ItemService itemService,
            SkillService skillService,
            TraitService traitService) {

        this.characterService = Objects.requireNonNull(characterService);
        this.userService = Objects.requireNonNull(userService);
        this.abilityService = Objects.requireNonNull(abilityService);
        this.weaponService = Objects.requireNonNull(weaponService);
        this.armorService = Objects.requireNonNull(armorService);
        this.itemService = Objects.requireNonNull(itemService);
        this.skillService = Objects.requireNonNull(skillService);
        this.traitService = Objects.requireNonNull(traitService);
    }

    public CharacterService getCharacterService() {
        return characterService;
    }

    public UserService getUserService() {
        return userService;
    }

    public AbilityService getAbilityService() {
        return abilityService;
    }

    public WeaponService getWeaponService() {
        return weaponService;
    }

    public ArmorService getArmorService() {
        return armorService;
    }

    public ItemService getItemService() {
        return itemService;
    }

    public SkillService getSkillService() {
        return skillService;
    }

    public TraitService getTraitService() {
        return traitService;
    }
}