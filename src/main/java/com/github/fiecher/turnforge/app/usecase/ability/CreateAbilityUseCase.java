package com.github.fiecher.turnforge.app.usecase.ability;

import com.github.fiecher.turnforge.app.dtos.requests.CreateAbilityRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.AbilityService;

import java.util.Objects;

public class CreateAbilityUseCase implements UseCase<CreateAbilityRequest, Long> {

    private final AbilityService abilityService;

    public CreateAbilityUseCase(AbilityService abilityService) {
        this.abilityService = Objects.requireNonNull(abilityService);
    }

    @Override
    public Long execute(CreateAbilityRequest input) {
        return abilityService.createAbility(input.name(), input.damage());
    }
}