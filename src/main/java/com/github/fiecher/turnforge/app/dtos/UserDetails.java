package com.github.fiecher.turnforge.app.dtos;

import com.github.fiecher.turnforge.domain.models.UserRole;

public record UserDetails(Long id, String login, UserRole role) {
}
