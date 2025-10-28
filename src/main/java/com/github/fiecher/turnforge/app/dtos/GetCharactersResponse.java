package com.github.fiecher.turnforge.app.dtos;

import java.util.List;

public record GetCharactersResponse(List<CharacterDetails> characters) {}