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
import com.github.fiecher.turnforge.infrastructure.db.postgres.PostgresConnectionManager;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresAbilityRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresArmorRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresCharacterRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresItemRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresSkillRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresTraitRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresUserRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.repositories.PostgresWeaponRepository;

public class PostgresRepositoryFactory {

    private final PostgresConnectionManager connectionManager;

    public PostgresRepositoryFactory(PostgresConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    
    public RepositoryContainer createAllRepositories() {
        AbilityRepository abilityRepository = new PostgresAbilityRepository(connectionManager);
        ArmorRepository armorRepository = new PostgresArmorRepository(connectionManager);
        CharacterRepository characterRepository = new PostgresCharacterRepository(connectionManager);
        ItemRepository itemRepository = new PostgresItemRepository(connectionManager);
        SkillRepository skillRepository = new PostgresSkillRepository(connectionManager);
        TraitRepository traitRepository = new PostgresTraitRepository(connectionManager);
        UserRepository userRepository = new PostgresUserRepository(connectionManager);
        WeaponRepository weaponRepository = new PostgresWeaponRepository(connectionManager);

        return new RepositoryContainer(abilityRepository, armorRepository, characterRepository, itemRepository, skillRepository, traitRepository, userRepository, weaponRepository);
    }
}
