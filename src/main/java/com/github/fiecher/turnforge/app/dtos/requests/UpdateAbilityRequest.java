package com.github.fiecher.turnforge.app.dtos.requests;

public record UpdateAbilityRequest(
        Long id,
        String name,
        String description,
        String image,
        String damage,
        String type,
        Short level,
        String time,
        String range,
        String components,
        String duration
) {
}