package com.github.fiecher.turnforge.app.dtos.requests;

public record UpdateArmorRequest(
        Long id,
        String name,
        String description,
        String image,
        short AC,
        String type,
        Double weight,
        Integer price
) {
}