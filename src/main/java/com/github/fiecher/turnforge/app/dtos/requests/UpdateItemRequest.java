package com.github.fiecher.turnforge.app.dtos.requests;

public record UpdateItemRequest(
        Long id,
        String name,
        String description,
        String image,
        Double weight,
        Integer price
) {
}