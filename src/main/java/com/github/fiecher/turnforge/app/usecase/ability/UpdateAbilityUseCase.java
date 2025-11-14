package com.github.fiecher.turnforge.app.usecase.ability;

import com.github.fiecher.turnforge.app.dtos.requests.UpdateAbilityRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Ability;
import com.github.fiecher.turnforge.domain.services.AbilityService;

import java.util.Objects;

public class UpdateAbilityUseCase implements UseCase<UpdateAbilityRequest, Ability> {

    private final AbilityService abilityService;

    public UpdateAbilityUseCase(AbilityService abilityService) {
        this.abilityService = Objects.requireNonNull(abilityService);
    }

    @Override
    public Ability execute(UpdateAbilityRequest input) {
        if (input.id() == null) {
            throw new IllegalArgumentException("Ability ID must be provided for update.");
        }

        Ability ability = new Ability(
                input.id(),
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

        return abilityService.updateAbility(ability);
    }
}