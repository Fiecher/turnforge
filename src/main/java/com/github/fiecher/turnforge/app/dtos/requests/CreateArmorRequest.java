package com.github.fiecher.turnforge.app.dtos.requests;

public record CreateArmorRequest(
        String name,
        String description,
        String image,
        short ac,
        String type,
        Double weight,
        Integer price
) {
}