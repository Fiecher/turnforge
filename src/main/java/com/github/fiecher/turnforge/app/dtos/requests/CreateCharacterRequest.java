package com.github.fiecher.turnforge.app.dtos.requests;

public record CreateCharacterRequest(Long userID, String name, String characterClass) {
}
