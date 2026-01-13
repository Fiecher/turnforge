package com.github.fiecher.turnforge.app.usecase.skill;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.models.Skill;
import com.github.fiecher.turnforge.domain.services.SkillService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GetSkillUseCase implements UseCase<Long, Optional<List<Skill>>> {
    private final SkillService service;
    public GetSkillUseCase(SkillService service) { this.service = Objects.requireNonNull(service); }

    @Override
    public Optional<List<Skill>> execute(Long id) {
        if (id == null) return Optional.of(service.getAllSkills());
        return service.getSkillById(id).map(List::of);
    }
}
