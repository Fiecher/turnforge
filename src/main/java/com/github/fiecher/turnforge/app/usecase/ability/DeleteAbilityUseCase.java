package com.github.fiecher.turnforge.app.usecase.ability;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.AbilityService;

import java.util.Objects;

public class DeleteAbilityUseCase implements UseCase<Long, Void> {

    private final AbilityService abilityService;

    public DeleteAbilityUseCase(AbilityService abilityService) {
        this.abilityService = Objects.requireNonNull(abilityService);
    }

    @Override
    public Void execute(Long abilityID) {
        abilityService.deleteAbilityByID(abilityID);
        return null;
    }
}