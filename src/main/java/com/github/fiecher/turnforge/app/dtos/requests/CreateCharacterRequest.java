package com.github.fiecher.turnforge.app.dtos.requests;

import java.util.List;

public record CreateCharacterRequest(
        Long userId,
        String name,
        String characterClass,
        Integer level,
        String race,
        Integer age,
        String size,
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
        String custom_skills
) {
}