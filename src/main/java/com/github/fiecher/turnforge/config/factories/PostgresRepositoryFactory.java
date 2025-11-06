package com.github.fiecher.turnforge.config.factories;

import com.github.fiecher.turnforge.config.containers.RepositoryContainer;
import com.github.fiecher.turnforge.domain.repositories.AbilityRepository;
import com.github.fiecher.turnforge.domain.repositories.ArmorRepository;
import com.github.fiecher.turnforge.domain.repositories.CharacterRepository;
import com.github.fiecher.turnforge.domain.repositories.ItemRepository;
import com.github.fiecher.turnforge.domain.repositories.SkillRepository;
import com.github.fiecher.turnforge.domain.repositories.TraitRepository;
import com.github.fiecher.turnforge.domain.repositories.UserRepository;
import com.github.fiecher.turnforge.domain.repositories.WeaponRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresArmorRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresCharacterRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresItemRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresSkillRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresTraitRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresUserRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresWeaponRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresAbilityRepository;

public class PostgresRepositoryFactory {
    
    public RepositoryContainer createAllRepositories() {
        AbilityRepository abilityRepository = new PostgresAbilityRepository();
        ArmorRepository armorRepository = new PostgresArmorRepository();
        CharacterRepository characterRepository = new PostgresCharacterRepository();
        ItemRepository itemRepository = new PostgresItemRepository();
        SkillRepository skillRepository = new PostgresSkillRepository();
        TraitRepository traitRepository = new PostgresTraitRepository();
        UserRepository userRepository = new PostgresUserRepository();
        WeaponRepository weaponRepository = new PostgresWeaponRepository();

        return new RepositoryContainer(abilityRepository, armorRepository, characterRepository, itemRepository, skillRepository, traitRepository, userRepository, weaponRepository);
    }
}
