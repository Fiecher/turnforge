package com.github.fiecher.turnforge.app.usecase.trait;

import com.github.fiecher.turnforge.app.dtos.requests.UpdateTraitRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Trait;
import com.github.fiecher.turnforge.domain.services.TraitService;
import java.util.Objects;

public class UpdateTraitUseCase implements UseCase<UpdateTraitRequest, Trait> {
    private final TraitService service;

    public UpdateTraitUseCase(TraitService service) {
        this.service = Objects.requireNonNull(service);
    }

    @Override
    public Trait execute(UpdateTraitRequest input) {
        if (input.id() == null) {
            throw new IllegalArgumentException("Trait ID must be provided for update.");
        }

        Trait trait = new Trait(
                input.id(),
                input.name(),
                input.description(),
                input.image(),
                input.prerequisites(),
                input.trait_type()
        );
        return service.updateTrait(trait);
    }
}