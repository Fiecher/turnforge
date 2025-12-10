package com.github.fiecher.turnforge.app.usecase.character;

import com.github.fiecher.turnforge.app.dtos.requests.UpdateCharacterRequest;
import com.github.fiecher.turnforge.app.dtos.responses.UpdateCharacterResponse;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.CharacterService;
import java.util.Objects;

public class UpdateCharacterUseCase implements UseCase<UpdateCharacterRequest, UpdateCharacterResponse> {

    private final CharacterService characterService;

    public UpdateCharacterUseCase(CharacterService characterService) {
        this.characterService = Objects.requireNonNull(characterService, "CharacterService cannot be null");
    }

    @Override
    public UpdateCharacterResponse execute(UpdateCharacterRequest input) {
        throw new UnsupportedOperationException("Use execute(UpdateCharacterRequest request, Long authenticatedUserId) for secure execution.");
    }


    public UpdateCharacterResponse execute(UpdateCharacterRequest request, Long authenticatedUserId) {
        characterService.updateCharacter(request, authenticatedUserId);

        return new UpdateCharacterResponse(request.characterID());
    }
}