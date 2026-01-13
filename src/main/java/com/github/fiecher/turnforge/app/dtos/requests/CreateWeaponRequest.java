package com.github.fiecher.turnforge.app.dtos.requests;

import java.math.BigDecimal;

public record CreateWeaponRequest(
        String name,
        String description,
        String image,
        String damage,
        String type,
        String properties,
        BigDecimal weight,
        Integer price
) {
}