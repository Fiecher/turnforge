package com.github.fiecher.turnforge.config.containers;

import com.github.fiecher.turnforge.domain.repositories.AbilityRepository;
import com.github.fiecher.turnforge.domain.repositories.ArmorRepository;
import com.github.fiecher.turnforge.domain.repositories.CharacterRepository;
import com.github.fiecher.turnforge.domain.repositories.ItemRepository;
import com.github.fiecher.turnforge.domain.repositories.SkillRepository;
import com.github.fiecher.turnforge.domain.repositories.TraitRepository;
import com.github.fiecher.turnforge.domain.repositories.UserRepository;
import com.github.fiecher.turnforge.domain.repositories.WeaponRepository;

public class RepositoryContainer {
    private final AbilityRepository abilityRepository;
    private final ArmorRepository armorRepository;
    private final CharacterRepository characterRepository;
    private final ItemRepository itemRepository;
    private final SkillRepository skillRepository;
    private final TraitRepository traitRepository;
    private final UserRepository userRepository;
    private final WeaponRepository weaponRepository;

    public RepositoryContainer(AbilityRepository abilityRepository, ArmorRepository armorRepository, CharacterRepository characterRepository, ItemRepository itemRepository, SkillRepository skillRepository, TraitRepository traitRepository, UserRepository userRepository, WeaponRepository weaponRepository) {
        this.abilityRepository = abilityRepository;
        this.armorRepository = armorRepository;
        this.characterRepository = characterRepository;
        this.itemRepository = itemRepository;
        this.skillRepository = skillRepository;
        this.traitRepository = traitRepository;
        this.userRepository = userRepository;
        this.weaponRepository = weaponRepository;
    }

    public AbilityRepository getAbilityRepository() {
        return abilityRepository;
    }

    public ArmorRepository getArmorRepository() {
        return armorRepository;
    }

    public CharacterRepository getCharacterRepository() {
        return characterRepository;
    }

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public SkillRepository getSkillRepository() {
        return skillRepository;
    }

    public TraitRepository getTraitRepository() {
        return traitRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public WeaponRepository getWeaponRepository() {
        return weaponRepository;
    }
}