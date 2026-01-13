package com.github.fiecher.turnforge.app.dtos.requests;

import com.github.fiecher.turnforge.domain.models.SizeType;

import java.util.List;


public record UpdateCharacterRequest(
        Long characterID,
        String name,
        String characterClass,
        Integer level,
        String race,
        Integer age,
        SizeType size,
        String subclass,
        String background,
        String description,
        Integer strength,
        Integer dexterity,
        Integer constitution,
        Integer intelligence,
        Integer wisdom,
        Integer charisma,
        String spellcasting_ability,
        Integer money,
        List<Long> weapons,
        List<Long> armor,
        List<Long> items,
        List<Long> traits,
        List<Long> abilities,
        List<Long> custom_skills
) {
}