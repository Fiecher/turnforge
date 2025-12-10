package com.github.fiecher.turnforge.app.dtos.requests;

public record UpdateTraitRequest(
        Long id,
        String name,
        String description,
        String image,
        String prerequisites,
        String trait_type
) {
}