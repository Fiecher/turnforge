package com.github.fiecher.turnforge.domain.services;


import com.github.fiecher.turnforge.app.dtos.requests.UpdateCharacterRequest;
import com.github.fiecher.turnforge.domain.models.*;
import com.github.fiecher.turnforge.domain.models.Character;
import com.github.fiecher.turnforge.domain.repositories.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CharacterService {

    private final CharacterRepository characterRepository;
    private final AbilityRepository abilityRepository;
    private final WeaponRepository weaponRepository;
    private final ArmorRepository armorRepository;
    private final ItemRepository itemRepository;
    private final TraitRepository traitRepository;
    private final SkillRepository skillRepository;

    public CharacterService(CharacterRepository characterRepository, AbilityRepository abilityRepository, WeaponRepository weaponRepository, ArmorRepository armorRepository, ItemRepository itemRepository, TraitRepository traitRepository, SkillRepository skillRepository) {
        this.characterRepository = Objects.requireNonNull(characterRepository);
        this.abilityRepository = Objects.requireNonNull(abilityRepository);
        this.weaponRepository = Objects.requireNonNull(weaponRepository);
        this.armorRepository = Objects.requireNonNull(armorRepository);
        this.itemRepository = Objects.requireNonNull(itemRepository);
        this.traitRepository = Objects.requireNonNull(traitRepository);
        this.skillRepository = Objects.requireNonNull(skillRepository);
    }

    public Long createNewCharacter(Long userID, String name, String characterClass) {
        if (userID == null) throw new IllegalArgumentException("User ID cannot be null.");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Character name cannot be empty.");
        if (characterClass == null || characterClass.trim().isEmpty()) throw new IllegalArgumentException("Character class cannot be empty.");

        if (characterRepository.existsByUserIDAndName(userID, name)) {
            throw new IllegalStateException("User " + userID + " already has a character named " + name);
        }

        Character newCharacter = new Character(userID, name, characterClass);

        return characterRepository.save(newCharacter);
    }

    public Optional<Character> getCharacterByID(Long characterID) {
        if (characterID == null) return Optional.empty();
        return characterRepository.findByID(characterID);
    }

    public List<Character> getAllUserCharacters(Long userId) {
        if (userId == null) return List.of();
        return characterRepository.findAllByUserID(userId);
    }

    public void updateCharacter(UpdateCharacterRequest request, Long userId) {
        if (request.characterID() == null) {
            throw new IllegalArgumentException("Character ID cannot be null.");
        }

        Optional<Character> existingCharacter = characterRepository.findByID(request.characterID());

        if (existingCharacter.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + request.characterID());
        }

        if (!Objects.equals(existingCharacter.get().getUserID(), userId)) {
            throw new SecurityException("Access denied: Character does not belong to the authenticated user.");
        }

        if (request.name() == null || request.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Character name cannot be empty.");
        }
        if (request.characterClass() == null || request.characterClass().trim().isEmpty()) {
            throw new IllegalArgumentException("Character class cannot be empty.");
        }

        validateEntityList(request.weapons(), weaponRepository, "Weapon");
        validateEntityList(request.armor(), armorRepository, "Armor");
        validateEntityList(request.items(), itemRepository, "Item");
        validateEntityList(request.traits(), traitRepository, "Trait");
        validateEntityList(request.abilities(), abilityRepository, "Ability");

        characterRepository.updateFullCharacter(request);
    }

    private void validateEntityList(List<Long> ids, Repository repository, String entityName) {
        if (ids == null) return;
        for (Long id : ids) {
            if (id == null) {
                System.err.println(entityName + " id list contains null, skipping validation.");
                continue;
            }
            if (repository.findByID(id).isEmpty()) {
                throw new IllegalArgumentException(entityName + " ID " + id + " not found in the database.");
            }
        }
    }


    public Character addAbilityToCharacter(Long characterID, Long abilityID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Optional<Ability> abilityOptional = abilityRepository.findByID(abilityID);
        if (abilityOptional.isEmpty()) {
            throw new IllegalArgumentException("Ability not found for ID: " + abilityID);
        }

        Character character = characterOptional.get();
        if (character.getAbilityIDs().contains(abilityID)) {
            return character;
        }

        character.addAbilityID(abilityID);
        return characterRepository.update(character);
    }

    public Character removeAbilityFromCharacter(Long characterID, Long abilityID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Character character = characterOptional.get();
        if (!character.getAbilityIDs().contains(abilityID)) {
            return character;
        }

        character.removeAbilityID(abilityID);
        return characterRepository.update(character);
    }

    public Character addWeaponToCharacter(Long characterID, Long weaponID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Optional<Weapon> weaponOptional = weaponRepository.findByID(weaponID);
        if (weaponOptional.isEmpty()) {
            throw new IllegalArgumentException("Weapon not found for ID: " + weaponID);
        }

        Character character = characterOptional.get();
        if (character.getWeaponIDs().contains(weaponID)) {
            return character;
        }

        character.addWeaponID(weaponID);
        return characterRepository.update(character);
    }

    public Character removeWeaponFromCharacter(Long characterID, Long weaponID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Character character = characterOptional.get();
        if (!character.getWeaponIDs().contains(weaponID)) {
            return character;
        }

        character.removeWeaponID(weaponID);
        return characterRepository.update(character);
    }

    public Character addArmorToCharacter(Long characterID, Long armorID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Optional<Armor> armorOptional = armorRepository.findByID(armorID);
        if (armorOptional.isEmpty()) {
            throw new IllegalArgumentException("Armor not found for ID: " + armorID);
        }

        Character character = characterOptional.get();
        if (character.getArmorIDs().contains(armorID)) {
            return character;
        }

        character.addArmorID(armorID);
        return characterRepository.update(character);
    }

    public Character removeArmorFromCharacter(Long characterID, Long armorID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Character character = characterOptional.get();
        if (!character.getArmorIDs().contains(armorID)) {
            return character;
        }

        character.removeArmorID(armorID);
        return characterRepository.update(character);
    }

    public Character addItemToCharacter(Long characterID, Long itemID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Optional<Item> itemOptional = itemRepository.findByID(itemID);
        if (itemOptional.isEmpty()) {
            throw new IllegalArgumentException("Item not found for ID: " + itemID);
        }

        Character character = characterOptional.get();
        if (character.getItemIDs().contains(itemID)) {
            return character;
        }

        character.addItemID(itemID);
        return characterRepository.update(character);
    }

    public Character removeItemFromCharacter(Long characterID, Long itemID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Character character = characterOptional.get();
        if (!character.getItemIDs().contains(itemID)) {
            return character;
        }

        character.removeItemID(itemID);
        return characterRepository.update(character);
    }

    public Character addTraitToCharacter(Long characterID, Long traitID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Optional<Trait> traitOptional = traitRepository.findByID(traitID);
        if (traitOptional.isEmpty()) {
            throw new IllegalArgumentException("Trait not found for ID: " + traitID);
        }

        Character character = characterOptional.get();
        if (character.getTraitIDs().contains(traitID)) {
            return character;
        }

        character.addTraitId(traitID);
        return characterRepository.update(character);
    }

    public Character removeTraitFromCharacter(Long characterID, Long traitID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Character character = characterOptional.get();
        if (!character.getTraitIDs().contains(traitID)) {
            return character;
        }

        character.removeTraitId(traitID);
        return characterRepository.update(character);
    }

    public Character addSkillToCharacter(Long characterID, Long skillID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Optional<Skill> skillOptional = skillRepository.findByID(skillID);
        if (skillOptional.isEmpty()) {
            throw new IllegalArgumentException("Skill not found for ID: " + skillID);
        }

        Character character = characterOptional.get();
        if (character.getSkillIDs().contains(skillID)) {
            return character;
        }

        character.addSkillID(skillID);
        return characterRepository.update(character);
    }

    public Character removeSkillFromCharacter(Long characterID, Long skillID) {
        Optional<Character> characterOptional = characterRepository.findByID(characterID);
        if (characterOptional.isEmpty()) {
            throw new IllegalArgumentException("Character not found for ID: " + characterID);
        }

        Character character = characterOptional.get();
        if (!character.getSkillIDs().contains(skillID)) {
            return character;
        }

        character.removeSkillID(skillID);
        return characterRepository.update(character);
    }

    public void deleteCharacter(Long characterID) {
        if (characterID == null) return;
        characterRepository.deleteByID(characterID);
    }
}