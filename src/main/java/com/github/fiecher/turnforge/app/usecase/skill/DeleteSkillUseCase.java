package com.github.fiecher.turnforge.app.usecase.skill;

import com.github.fiecher.turnforge.app.usecase.UseCase;
import com.github.fiecher.turnforge.domain.services.SkillService;

import java.util.Objects;

public class DeleteSkillUseCase implements UseCase<Long, Void> {
    private final SkillService service;
    public DeleteSkillUseCase(SkillService service) { this.service = Objects.requireNonNull(service); }
    @Override
    public Void execute(Long id) { service.deleteSkillByID(id); return null; }
}
