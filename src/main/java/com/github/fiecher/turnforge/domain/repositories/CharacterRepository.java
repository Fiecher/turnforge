package com.github.fiecher.turnforge.domain.repositories;

import com.github.fiecher.turnforge.app.dtos.requests.UpdateCharacterRequest;
import com.github.fiecher.turnforge.domain.models.Character;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends Repository<Character, Long> {

    List<Character> findAllByUserID(Long userID);

    Optional<Character> findByUserIDAndName(Long userID, String name);

    void updateFullCharacter(UpdateCharacterRequest request);

    boolean existsByUserIDAndName(Long userID, String name);
}