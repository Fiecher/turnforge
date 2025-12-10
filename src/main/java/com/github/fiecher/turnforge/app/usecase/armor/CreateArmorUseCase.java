package com.github.fiecher.turnforge.app.usecase.armor;

import com.github.fiecher.turnforge.app.dtos.requests.CreateArmorRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.ArmorService;
import java.util.Objects;

public class CreateArmorUseCase implements UseCase<CreateArmorRequest, Long> {
    private final ArmorService service;

    public CreateArmorUseCase(ArmorService service) {
        this.service = Objects.requireNonNull(service);
    }

    @Override
    public Long execute(CreateArmorRequest input) {
        return service.createArmor(
                input.name()
        );
    }
}