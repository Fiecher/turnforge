package com.github.fiecher.turnforge.infrastructure.db.inmemory;

import com.github.fiecher.turnforge.domain.models.Character;
import com.github.fiecher.turnforge.domain.repositories.CharacterRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryCharacterRepository implements CharacterRepository {

    private final Map<Long, Character> charactersByID = new ConcurrentHashMap<>();
    private final Map<String, Character> charactersByUniqueKey = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    private String generateUniqueKey(Long userId, String name) {
        return userId + "_" + name.toLowerCase();
    }

    @Override
    public Long save(Character character) {
        Objects.requireNonNull(character);

        String newKey = generateUniqueKey(character.getUserID(), character.getName());

        if (character.getID() == null) {
            if (charactersByUniqueKey.containsKey(newKey)) {
                throw new IllegalStateException("Character with name '" + character.getName() + "' already exists for user " + character.getUserID());
            }
            character.setID(idGenerator.getAndIncrement());
        } else {
            Character existingCharacter = charactersByID.get(character.getID());
            if (existingCharacter != null && !existingCharacter.getUserID().equals(character.getUserID())) {
                throw new IllegalStateException("Character ID " + character.getID() + " belongs to a different user.");
            }

            assert existingCharacter != null;
            String oldKey = generateUniqueKey(character.getUserID(), existingCharacter.getName());
            if (!oldKey.equals(newKey)) {
                if (charactersByUniqueKey.containsKey(newKey)) {
                    throw new IllegalStateException("Name '" + character.getName() + "' is already in use by another character of user " + character.getUserID());
                }
                charactersByUniqueKey.remove(oldKey);
            }
        }

        charactersByID.put(character.getID(), character);
        charactersByUniqueKey.put(newKey, character);

        return character.getID();
    }

    @Override
    public Optional<Character> findByID(Long characterId) {
        if (characterId == null) return Optional.empty();
        return Optional.ofNullable(charactersByID.get(characterId));
    }

    @Override
    public List<Character> findAllByUserID(Long userId) {
        if (userId == null) return Collections.emptyList();
        return charactersByID.values().stream()
                .filter(c -> c.getUserID().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Character> findByUserIDAndName(Long userId, String name) {
        if (userId == null || name == null || name.trim().isEmpty()) return Optional.empty();
        String key = generateUniqueKey(userId, name);
        return Optional.ofNullable(charactersByUniqueKey.get(key));
    }

    @Override
    public List<Character> findAll() {
        return new ArrayList<>(charactersByID.values());
    }

    @Override
    public Character update(Character character) {
        if (character.getID() == null || !charactersByID.containsKey(character.getID())) {
            throw new IllegalArgumentException("Character ID must be present and exist for update.");
        }
        this.save(character);
        return character;
    }

    @Override
    public void deleteByID(Long characterId) {
        if (characterId == null) return;
        Character character = charactersByID.remove(characterId);
        if (character != null) {
            String key = generateUniqueKey(character.getUserID(), character.getName());
            charactersByUniqueKey.remove(key);
        }
    }

    @Override
    public boolean existsByUserIDAndName(Long userId, String name) {
        if (userId == null || name == null || name.trim().isEmpty()) return false;
        String key = generateUniqueKey(userId, name);
        return charactersByUniqueKey.containsKey(key);
    }
}