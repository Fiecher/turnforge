package com.github.fiecher.turnforge.app.dtos.requests;

public record CreateTraitRequest(
        String name,
        String description,
        String image,
        String prerequisites,
        String trait_type
) {
}