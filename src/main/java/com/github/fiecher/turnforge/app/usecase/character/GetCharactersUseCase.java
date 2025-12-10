package com.github.fiecher.turnforge.app.usecase.character;

import com.github.fiecher.turnforge.app.dtos.requests.GetCharactersRequest;
import com.github.fiecher.turnforge.app.dtos.responses.CharacterDetails;
import com.github.fiecher.turnforge.app.dtos.responses.GetCharactersResponse;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.*;
import com.github.fiecher.turnforge.domain.models.Character;
import com.github.fiecher.turnforge.domain.repositories.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GetCharactersUseCase implements UseCase<GetCharactersRequest, GetCharactersResponse> {

    private final CharacterRepository characterRepository;
    private final AbilityRepository abilityRepository;
    private final SkillRepository skillRepository;
    private final WeaponRepository weaponRepository;
    private final ArmorRepository armorRepository;
    private final ItemRepository itemRepository;
    private final TraitRepository traitRepository;

    public GetCharactersUseCase(
            CharacterRepository characterRepository,
            AbilityRepository abilityRepository,
            SkillRepository skillRepository,
            WeaponRepository weaponRepository,
            ArmorRepository armorRepository,
            ItemRepository itemRepository,
            TraitRepository traitRepository) {

        this.characterRepository = Objects.requireNonNull(characterRepository);
        this.abilityRepository = Objects.requireNonNull(abilityRepository);
        this.skillRepository = Objects.requireNonNull(skillRepository);
        this.weaponRepository = Objects.requireNonNull(weaponRepository);
        this.armorRepository = Objects.requireNonNull(armorRepository);
        this.itemRepository = Objects.requireNonNull(itemRepository);
        this.traitRepository = Objects.requireNonNull(traitRepository);
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
        List<String> abilityNames = abilityRepository.findAllByCharacterID(character.getID())
                .stream()
                .map(Ability::getName)
                .collect(Collectors.toList());

        List<String> skillNames = skillRepository.findAllByCharacterID(character.getID())
                .stream()
                .map(Skill::getName)
                .collect(Collectors.toList());

        List<String> weaponNames = weaponRepository.findAllByCharacterID(character.getID())
                .stream()
                .map(Weapon::getName)
                .collect(Collectors.toList());

        List<String> armorNames = armorRepository.findAllByCharacterID(character.getID())
                .stream()
                .map(Armor::getName)
                .collect(Collectors.toList());

        List<String> itemNames = itemRepository.findAllByCharacterID(character.getID())
                .stream()
                .map(Item::getName)
                .collect(Collectors.toList());

        List<String> traitNames = traitRepository.findAllByCharacterID(character.getID())
                .stream()
                .map(Trait::getName)
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

                character.getSubclass(),
                character.getBackground(),
                character.getAge(),
                character.getSize().name(),
                character.getSpellcastingAbility(),
                character.getMoney(),

                abilityNames,
                skillNames,
                weaponNames,
                armorNames,
                itemNames,
                traitNames
        );
    }


}