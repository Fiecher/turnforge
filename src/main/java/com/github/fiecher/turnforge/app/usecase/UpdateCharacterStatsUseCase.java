package com.github.fiecher.turnforge.app.usecase;

import com.github.fiecher.turnforge.app.dtos.requests.UpdateStatsRequest;
import com.github.fiecher.turnforge.app.dtos.responses.UpdateStatsResponse;
import com.github.fiecher.turnforge.domain.services.CharacterService;
import java.util.Objects;

public class UpdateCharacterStatsUseCase implements UseCase<UpdateStatsRequest, UpdateStatsResponse> {

    private final CharacterService characterService;

    public UpdateCharacterStatsUseCase(CharacterService characterService) {
        this.characterService = Objects.requireNonNull(characterService);
    }

    @Override
    public UpdateStatsResponse execute(UpdateStatsRequest input) {
        characterService.updateCharacterStats(
                input.characterID(),
                input.strength(),
                input.dexterity(),
                input.constitution(),
                input.intelligence(),
                input.wisdom(),
                input.charisma()
        );

        return new UpdateStatsResponse(input.characterID());
    }
}