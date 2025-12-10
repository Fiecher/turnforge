package com.github.fiecher.turnforge.app.dtos.requests;

public record CreateItemRequest(
        String name,
        String description,
        String image,
        Double weight,
        Integer price
) {
}