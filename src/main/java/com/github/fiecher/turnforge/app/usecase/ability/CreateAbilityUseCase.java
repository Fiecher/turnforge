package com.github.fiecher.turnforge.app.usecase.ability;

import com.github.fiecher.turnforge.app.dtos.requests.CreateAbilityRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Ability;
import com.github.fiecher.turnforge.domain.services.AbilityService;

import java.util.Objects;

public class CreateAbilityUseCase implements UseCase<CreateAbilityRequest, Long> {

    private final AbilityService abilityService;

    public CreateAbilityUseCase(AbilityService abilityService) {
        this.abilityService = Objects.requireNonNull(abilityService);
    }

    @Override
    public Long execute(CreateAbilityRequest input) {
        Ability ability = new Ability(
                null,
                input.name(),
                input.description(),
                input.image(),
                input.damage(),
                input.type(),
                input.level() != null ? input.level() : 0,
                input.time(),
                input.range(),
                input.components(),
                input.duration()
        );

        return abilityService.createAbility(ability);
    }
}