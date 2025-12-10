package com.github.fiecher.turnforge.config.containers;

import com.github.fiecher.turnforge.presentation.servlet.api.*;
import java.util.Objects;

public class ServletContainer {
    private final UserServlet userServlet;
    private final CharacterServlet characterServlet;
    private final AbilityServlet abilityServlet;
    private final WeaponServlet weaponServlet;
    private final ArmorServlet armorServlet;
    private final ItemServlet itemServlet;
    private final SkillServlet skillServlet;
    private final TraitServlet traitServlet;

    public ServletContainer(UserServlet userServlet, CharacterServlet characterServlet, AbilityServlet abilityServlet, WeaponServlet weaponServlet, ArmorServlet armorServlet, ItemServlet itemServlet, SkillServlet skillServlet, TraitServlet traitServlet) {
        this.userServlet = Objects.requireNonNull(userServlet);
        this.characterServlet = Objects.requireNonNull(characterServlet);
        this.abilityServlet = Objects.requireNonNull(abilityServlet);
        this.weaponServlet = Objects.requireNonNull(weaponServlet);
        this.armorServlet = Objects.requireNonNull(armorServlet);
        this.itemServlet = Objects.requireNonNull(itemServlet);
        this.skillServlet = Objects.requireNonNull(skillServlet);
        this.traitServlet = Objects.requireNonNull(traitServlet);
    }

    public UserServlet getUserServlet() { return userServlet; }
    public CharacterServlet getCharacterServlet() { return characterServlet; }
    public AbilityServlet getAbilityServlet() { return abilityServlet; }
    public WeaponServlet getWeaponServlet() { return weaponServlet; }
    public ArmorServlet getArmorServlet() { return armorServlet; }
    public ItemServlet getItemServlet() { return itemServlet; }
    public SkillServlet getSkillServlet() { return skillServlet; }
    public TraitServlet getTraitServlet() { return traitServlet; }
}