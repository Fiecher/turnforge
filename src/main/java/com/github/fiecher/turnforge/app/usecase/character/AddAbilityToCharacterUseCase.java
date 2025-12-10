package com.github.fiecher.turnforge.app.usecase.character;

import com.github.fiecher.turnforge.app.dtos.requests.AddAbilityRequest;
import com.github.fiecher.turnforge.app.dtos.responses.AddAbilityResponse;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.CharacterService;

import java.util.Objects;

public class AddAbilityToCharacterUseCase implements UseCase<AddAbilityRequest, AddAbilityResponse> {

    private final CharacterService characterService;

    public AddAbilityToCharacterUseCase(CharacterService characterService) {
        this.characterService = Objects.requireNonNull(characterService);
    }

    @Override
    public AddAbilityResponse execute(AddAbilityRequest input) {
        Objects.requireNonNull(input, "Request cannot be null");
        Long characterId = Objects.requireNonNull(input.characterID(), "characterID is required");
        Long abilityId = Objects.requireNonNull(input.abilityID(), "abilityID is required");

        characterService.addAbilityToCharacter(characterId, abilityId);
        return new AddAbilityResponse(characterId);
    }

}