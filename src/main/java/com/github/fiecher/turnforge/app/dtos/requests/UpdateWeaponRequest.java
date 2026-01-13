package com.github.fiecher.turnforge.app.dtos.requests;

public record UpdateWeaponRequest(
        Long id,
        String name,
        String description,
        String image,
        String damage,
        String type,
        String properties,
        Double weight,
        Integer price
) {
}