package com.github.fiecher.turnforge.app.dtos;

public record CreateCharacterRequest(Long userID, String name, String characterClass) {
}
