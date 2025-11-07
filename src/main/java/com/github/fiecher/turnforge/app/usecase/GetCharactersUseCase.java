package com.github.fiecher.turnforge.app.usecase;

import com.github.fiecher.turnforge.app.dtos.responses.CharacterDetails;
import com.github.fiecher.turnforge.app.dtos.requests.GetCharactersRequest;
import com.github.fiecher.turnforge.app.dtos.responses.GetCharactersResponse;
import com.github.fiecher.turnforge.domain.models.Ability;
import com.github.fiecher.turnforge.domain.models.Skill;
import com.github.fiecher.turnforge.domain.models.Armor;
import com.github.fiecher.turnforge.domain.models.Weapon;
import com.github.fiecher.turnforge.domain.models.Item;
import com.github.fiecher.turnforge.domain.models.Character;
import com.github.fiecher.turnforge.domain.repositories.AbilityRepository;
import com.github.fiecher.turnforge.domain.repositories.CharacterRepository;
import com.github.fiecher.turnforge.domain.repositories.SkillRepository;
import com.github.fiecher.turnforge.domain.repositories.WeaponRepository;
import com.github.fiecher.turnforge.domain.repositories.ArmorRepository;
import com.github.fiecher.turnforge.domain.repositories.ItemRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class GetCharactersUseCase implements UseCase<GetCharactersRequest, GetCharactersResponse> {

    private final CharacterRepository characterRepository;
    private final AbilityRepository abilityRepository;
    private final SkillRepository skillRepository;
    private final WeaponRepository weaponRepository;
    private final ArmorRepository armorRepository;
    private final ItemRepository itemRepository;

    public GetCharactersUseCase(
            CharacterRepository characterRepository,
            AbilityRepository abilityRepository,
            SkillRepository skillRepository,
            WeaponRepository weaponRepository,
            ArmorRepository armorRepository,
            ItemRepository itemRepository) {

        this.characterRepository = Objects.requireNonNull(characterRepository);
        this.abilityRepository = Objects.requireNonNull(abilityRepository);
        this.skillRepository = Objects.requireNonNull(skillRepository);
        this.weaponRepository = Objects.requireNonNull(weaponRepository);
        this.armorRepository = Objects.requireNonNull(armorRepository);
        this.itemRepository = Objects.requireNonNull(itemRepository);
    }

    @Override
    public GetCharactersResponse execute(GetCharactersRequest input) {
        List<Character> userCharacters = characterRepository.findAllByUserID(input.userID());

        List<CharacterDetails> detailsList = userCharacters.stream()
                .map(this::mapToDetails)
                .collect(Collectors.toList());

        return new GetCharactersResponse(detailsList);
    }


    private CharacterDetails mapToDetails(Character character) {
        List<String> abilityNames = character.getAbilityIDs().stream()
                .map(abilityRepository::findByID)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Ability::getName)
                .collect(Collectors.toList());

        List<String> skillNames = character.getSkillIDs().stream()
                .map(skillRepository::findByID)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Skill::getName)
                .collect(Collectors.toList());

        List<String> weaponNames = character.getWeaponIDs().stream()
                .map(weaponRepository::findByID)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Weapon::getName)
                .collect(Collectors.toList());

        List<String> armorNames = character.getArmorIDs().stream()
                .map(armorRepository::findByID)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Armor::getName)
                .collect(Collectors.toList());

        List<String> itemNames = character.getItemIDs().stream()
                .map(itemRepository::findByID)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Item::getName)
                .collect(Collectors.toList());


        return new CharacterDetails(
                character.getID(),
                character.getName(),
                character.getClass_(),
                character.getLevel(),

                character.getStrength(),
                character.getDexterity(),
                character.getConstitution(),
                character.getIntelligence(),
                character.getWisdom(),
                character.getCharisma(),

                character.getDescription(),
                character.getImage(),
                character.getRace(),

                abilityNames,
                skillNames,
                weaponNames,
                armorNames,
                itemNames
        );
    }
}