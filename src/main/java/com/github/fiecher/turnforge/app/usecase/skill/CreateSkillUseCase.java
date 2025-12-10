package com.github.fiecher.turnforge.app.usecase.skill;

import com.github.fiecher.turnforge.app.dtos.requests.CreateSkillRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.SkillService;
import java.util.Objects;

public class CreateSkillUseCase implements UseCase<CreateSkillRequest, Long> {
    private final SkillService service;

    public CreateSkillUseCase(SkillService service) {
        this.service = Objects.requireNonNull(service);
    }

    @Override
    public Long execute(CreateSkillRequest input) {
        return service.createSkill(input.name());
    }
}