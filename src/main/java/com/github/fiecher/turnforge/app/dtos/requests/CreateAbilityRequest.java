package com.github.fiecher.turnforge.app.dtos.requests;

public record CreateAbilityRequest(
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