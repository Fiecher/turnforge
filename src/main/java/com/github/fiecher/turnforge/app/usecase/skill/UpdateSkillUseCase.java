package com.github.fiecher.turnforge.app.usecase.skill;

import com.github.fiecher.turnforge.app.dtos.requests.UpdateSkillRequest;
import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Skill;
import com.github.fiecher.turnforge.domain.services.SkillService;
import java.util.Objects;

public class UpdateSkillUseCase implements UseCase<UpdateSkillRequest, Skill> {
    private final SkillService service;

    public UpdateSkillUseCase(SkillService service) {
        this.service = Objects.requireNonNull(service);
    }

    @Override
    public Skill execute(UpdateSkillRequest input) {
        if (input.id() == null) {
            throw new IllegalArgumentException("Skill ID must be provided for update.");
        }

        Skill skill = new Skill(
                input.id(),
                input.name(),
                input.description()
        );
        return service.updateSkill(skill);
    }
}