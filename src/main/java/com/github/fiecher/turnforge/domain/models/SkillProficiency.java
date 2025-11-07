package com.github.fiecher.turnforge.domain.models;

import java.util.Objects;

public class SkillProficiency {
    private final Long skillId;
    private final String skillName;
    private Boolean isProficient;
    private Boolean isExpertise;

    public SkillProficiency(Long skillId, String skillName, Boolean isProficient, Boolean isExpertise) {
        this.skillId = Objects.requireNonNull(skillId);
        this.skillName = Objects.requireNonNull(skillName);
        this.isProficient = Objects.requireNonNull(isProficient);
        this.isExpertise = Objects.requireNonNull(isExpertise);
    }

    public Long getSkillID() {
        return skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public Boolean getIsProficient() {
        return isProficient;
    }

    public void setIsProficient(Boolean proficient) {
        isProficient = proficient;
    }

    public Boolean getIsExpertise() {
        return isExpertise;
    }

    public void setIsExpertise(Boolean expertise) {
        isExpertise = expertise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillProficiency that = (SkillProficiency) o;
        return Objects.equals(skillId, that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skillId);
    }
}