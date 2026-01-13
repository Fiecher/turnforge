package com.github.fiecher.turnforge.config.containers;

import com.github.fiecher.turnforge.domain.repositories.AbilityRepository;
import com.github.fiecher.turnforge.domain.repositories.ArmorRepository;
import com.github.fiecher.turnforge.domain.repositories.CharacterRepository;
import com.github.fiecher.turnforge.domain.repositories.ItemRepository;
import com.github.fiecher.turnforge.domain.repositories.SkillRepository;
import com.github.fiecher.turnforge.domain.repositories.TraitRepository;
import com.github.fiecher.turnforge.domain.repositories.UserRepository;
import com.github.fiecher.turnforge.domain.repositories.WeaponRepository;

public record RepositoryContainer(
        AbilityRepository abilityRepository,
        ArmorRepository armorRepository,
        CharacterRepository characterRepository,
        ItemRepository itemRepository,
        SkillRepository skillRepository,
        TraitRepository traitRepository,
        UserRepository userRepository,
        WeaponRepository weaponRepository) {
}
