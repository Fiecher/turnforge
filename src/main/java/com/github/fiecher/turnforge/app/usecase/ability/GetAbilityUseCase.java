package com.github.fiecher.turnforge.app.usecase.ability;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Ability;
import com.github.fiecher.turnforge.domain.services.AbilityService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GetAbilityUseCase implements UseCase<Long, Optional<List<Ability>>> {

    private final AbilityService abilityService;

    public GetAbilityUseCase(AbilityService abilityService) {
        this.abilityService = Objects.requireNonNull(abilityService);
    }

    @Override
    public Optional<List<Ability>> execute(Long abilityID) {
        if (abilityID == null) {
            return Optional.of(abilityService.getAllAbilities());
        } else {
            return abilityService.getAbilityByID(abilityID)
                    .map(List::of);
        }
    }
}