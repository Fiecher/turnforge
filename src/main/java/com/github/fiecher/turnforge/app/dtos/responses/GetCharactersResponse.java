package com.github.fiecher.turnforge.app.dtos.responses;

import java.util.List;

public record GetCharactersResponse(List<CharacterDetails> characters) {}