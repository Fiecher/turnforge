package com.github.fiecher.turnforge.app.usecase.trait;

import com.github.fiecher.turnforge.app.dtos.requests.CreateTraitRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.TraitService;
import java.util.Objects;

public class CreateTraitUseCase implements UseCase<CreateTraitRequest, Long> {
    private final TraitService service;

    public CreateTraitUseCase(TraitService service) {
        this.service = Objects.requireNonNull(service);
    }

    @Override
    public Long execute(CreateTraitRequest input) {
        return service.createTrait(
                input.name()
        );
    }
}