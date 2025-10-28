package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.domain.models.Character;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository {

    Long save(Character character);

    Optional<Character> findByID(Long characterID);

    List<Character> findAllByUserID(Long userID);

    Optional<Character> findByUserIDAndName(Long userID, String name);

    List<Character> findAll();

    Character update(Character character);

    void deleteByID(Long characterID);

    boolean existsByUserIDAndName(Long userID, String name);
}