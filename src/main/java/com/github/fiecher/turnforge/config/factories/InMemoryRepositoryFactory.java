package com.github.fiecher.turnforge.config.factories;

import com.github.fiecher.turnforge.config.containers.RepositoryContainer;
import com.github.fiecher.turnforge.domain.repositories.*;
import com.github.fiecher.turnforge.infrastructure.db.inmemory.InMemoryAbilityRepository;
import com.github.fiecher.turnforge.infrastructure.db.inmemory.InMemoryArmorRepository;
import com.github.fiecher.turnforge.infrastructure.db.inmemory.InMemoryCharacterRepository;
import com.github.fiecher.turnforge.infrastructure.db.inmemory.InMemoryItemRepository;
import com.github.fiecher.turnforge.infrastructure.db.inmemory.InMemorySkillRepository;
import com.github.fiecher.turnforge.infrastructure.db.inmemory.InMemoryTraitRepository;
import com.github.fiecher.turnforge.infrastructure.db.inmemory.InMemoryUserRepository;
import com.github.fiecher.turnforge.infrastructure.db.inmemory.InMemoryWeaponRepository;

public class InMemoryRepositoryFactory {

    public RepositoryContainer createAllRepositories() {
        AbilityRepository abilityRepository = new InMemoryAbilityRepository();
        ArmorRepository armorRepository = new InMemoryArmorRepository();
        CharacterRepository characterRepository = new InMemoryCharacterRepository();
        ItemRepository itemRepository = new InMemoryItemRepository();
        SkillRepository skillRepository = new InMemorySkillRepository();
        TraitRepository traitRepository = new InMemoryTraitRepository();
        UserRepository userRepository = new InMemoryUserRepository();
        WeaponRepository weaponRepository = new InMemoryWeaponRepository();

        return new RepositoryContainer(abilityRepository, armorRepository, characterRepository, itemRepository, skillRepository, traitRepository, userRepository, weaponRepository);
    }
}